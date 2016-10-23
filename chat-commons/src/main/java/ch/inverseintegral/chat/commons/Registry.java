package ch.inverseintegral.chat.commons;

import ch.inverseintegral.chat.commons.listeners.ChannelListener;
import ch.inverseintegral.chat.commons.listeners.Listener;
import ch.inverseintegral.chat.commons.packets.Packet;
import io.netty.channel.Channel;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.*;

/**
 * A Registry contains listeners to specific packets.
 * This class supports a registration and an unregistration mechanism.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 * @see Listener
 * @see ChannelListener
 */
public final class Registry {

    /**
     * The lookup instance that obtains the method handles.
     */
    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    /**
     * A map of packet classes and method containers.
     */
    private static final Map<Class<? extends Packet>, Set<MethodContainer>> listeners = new HashMap<>();

    /**
     * Registers new listener to the context.
     *
     * @param invoker The object that contains {@link Listener listener} or
     *                {@link ChannelListener channel listener}annotated methods.
     */
    public static void registerListener(Object invoker) {
        // Get all methods from this class.
        List<Method> methods = Arrays.asList(invoker.getClass().getDeclaredMethods());

        for (Method method : methods) {

            Listener listener = method.getAnnotation(Listener.class);
            ChannelListener channelListener = method.getAnnotation(ChannelListener.class);

            if (listener != null) {
                registerListener(invoker, method);
            } else if (channelListener != null) {
                registerChannelListener(invoker, method);
            }
        }
    }

    private static void registerListener(Object invoker, Method method) {
        if (method.getParameterCount() != 1) {
            throw new IllegalArgumentException("A listener method must have one parameter");
        } else {
            Class<?> firstParameter = method.getParameterTypes()[0];

            if (!Packet.class.isAssignableFrom(firstParameter)) {
                throw new IllegalArgumentException("The parameter must be of the type Packet");
            } else {
                Class<? extends Packet> packetParameter = (Class<? extends Packet>) firstParameter;
                addMethodContainer(packetParameter, invoker, method, false);
            }
        }
    }

    private static void registerChannelListener(Object invoker, Method method) {
        if (method.getParameterCount() != 2) {
            throw new IllegalArgumentException("A channel listener method must have two parameter");
        } else {
            Class<?> firstParameter = method.getParameterTypes()[0];
            Class<?> secondParameter = method.getParameterTypes()[1];

            if (!Packet.class.isAssignableFrom(firstParameter) || !Channel.class.isAssignableFrom(secondParameter)) {
                throw new IllegalArgumentException("The parameters must be of the type Packet and Channel");
            } else {
                Class<? extends Packet> packetParameter = (Class<? extends Packet>) firstParameter;
                addMethodContainer(packetParameter, invoker, method, true);
            }
        }
    }

    private static void addMethodContainer(Class<? extends Packet> packetClass, Object invoker, Method method, boolean channelListener) {
        try {
            MethodContainer methodContainer = new MethodContainer(invoker, lookup.unreflect(method), channelListener);

            // Create a new set if there is none present. Add the new method container to the set.
            listeners.computeIfAbsent(packetClass, aClass -> new HashSet<>());
            listeners.get(packetClass).add(methodContainer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unregisters all listeners that the given object contains.
     *
     * @param invoker The object that contains {@link Listener listener}
     *                or {@link ChannelListener channel listener} annotated methods.
     */
    public static void unregisterListener(Object invoker) {
        for (Map.Entry<Class<? extends Packet>, Set<MethodContainer>> classSetEntry : listeners.entrySet()) {

            // Match on the invoker object and remove the specific method container.
            classSetEntry.getValue().stream()
                    .filter(methodContainer -> methodContainer.getInvoker().equals(invoker))
                    .forEach(methodContainer -> listeners.get(classSetEntry.getKey()).remove(methodContainer));
        }
    }

    /**
     * Gets all the method containers for a given packet class.
     *
     * @param packetClass The packet implementation class.
     * @return Returns the found method containers or null.
     */
    public static Set<MethodContainer> getListeners(Class<? extends Packet> packetClass) {
        return listeners.get(packetClass);
    }

    /**
     * This class contains an invoker, a method handle and a boolean flag.
     * These attributes can be used to invoke a method on a specific object.
     * The flag indicated whether this method is a channel listener or a
     * normal listener.
     *
     * @author Inverse Integral
     * @version 1.0
     * @since 1.0
     */
    public static final class MethodContainer {

        private final Object invoker;
        private final MethodHandle methodHandle;
        private final boolean channelListener;

        MethodContainer(final Object invoker, final MethodHandle methodHandle, final boolean channelListener) {
            this.invoker = invoker;
            this.methodHandle = methodHandle;
            this.channelListener = channelListener;
        }

        public Object getInvoker() {
            return invoker;
        }

        public MethodHandle getMethodHandle() {
            return methodHandle;
        }

        public boolean isChannelListener() {
            return channelListener;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MethodContainer that = (MethodContainer) o;

            return channelListener == that.channelListener &&
                    invoker.equals(that.invoker) &&
                    methodHandle.equals(that.methodHandle);

        }

        @Override
        public int hashCode() {
            int result = invoker.hashCode();
            result = 31 * result + methodHandle.hashCode();
            result = 31 * result + (channelListener ? 1 : 0);
            return result;
        }
    }

}
