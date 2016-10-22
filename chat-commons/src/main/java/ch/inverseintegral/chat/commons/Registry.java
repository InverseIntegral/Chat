package ch.inverseintegral.chat.commons;

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
     * @param invoker The object that contains {@link Listener listener} annotated methods.
     */
    public static void registerListener(Object invoker) {

        // Get all methods from this class.
        List<Method> methods = Arrays.asList(invoker.getClass().getDeclaredMethods());

        for (Method method : methods) {

            // Get the annotation instance to check if there is an annotation present.
            Listener listener = method.getAnnotation(Listener.class);
            if (listener != null) {

                // A listener should have exactly one parameter.
                if (method.getParameterCount() == 0 || method.getParameterCount() > 2) {
                    throw new IllegalArgumentException("A listener method must exactly have two parameters");
                } else {

                    // Get the first parameter (packet implementation).
                    Class<?> firstParameter = method.getParameterTypes()[0];
                    Class<?> secondParameter = method.getParameterTypes()[1];

                    if (!Packet.class.isAssignableFrom(firstParameter) || !Channel.class.isAssignableFrom(secondParameter)) {
                        throw new IllegalArgumentException("The parameter must be of the type Packet and Channel");
                    } else {
                        Class<? extends Packet> packetParameter = (Class<? extends Packet>) firstParameter;

                        try {
                            MethodContainer methodContainer = new MethodContainer(invoker, lookup.unreflect(method));

                            // Create a new set if there is none present. Add the new method container to the set.
                            listeners.computeIfAbsent(packetParameter, aClass -> new HashSet<>());
                            listeners.get(packetParameter).add(methodContainer);

                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * Unregisters all listeners that the given object contains.
     *
     * @param invoker The object that contains {@link Listener listener} annotated methods.
     */
    public static void unregisterListener(Object invoker) {
        for (Map.Entry<Class<? extends Packet>, Set<MethodContainer>> classSetEntry : listeners.entrySet()) {

            // Match on the invoker object and remove the specific method container.
            classSetEntry.getValue().stream()
                    .filter(methodContainer -> methodContainer.getInvoker().equals(invoker))
                    .forEach(methodContainer -> {
                        listeners.get(classSetEntry.getKey()).remove(methodContainer);
                        System.out.println("Unregistered a listener for the packet " + classSetEntry.getKey().getName());
                    });
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
     * This class contains an invoker and a method handle.
     * These attributes can be used to invoke a method on a specific objects.
     * In this case it is used for listener methods.
     */
    public static final class MethodContainer {

        private final Object invoker;
        private final MethodHandle methodHandle;

        public MethodContainer(final Object invoker, final MethodHandle methodHandle) {
            this.invoker = invoker;
            this.methodHandle = methodHandle;
        }

        public Object getInvoker() {
            return invoker;
        }

        public MethodHandle getMethodHandle() {
            return methodHandle;
        }

    }

}
