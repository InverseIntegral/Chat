package ch.inverseintegral.chat.commons.handlers;

import ch.inverseintegral.chat.commons.Registry;
import ch.inverseintegral.chat.commons.packets.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Set;

/**
 * A PacketHandler handles incoming and outgoing packets.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class PacketHandler extends SimpleChannelInboundHandler<Packet> {

    /**
     * All open channel connections.
     */
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        Class<? extends Packet> packetClass = packet.getClass();
        Set<Registry.MethodContainer> methodContainers = Registry.getListeners(packetClass);

        if (methodContainers != null) {

            // Call all the method handles using the invoker object and the received packet.
            for (Registry.MethodContainer methodContainer : Registry.getListeners(packetClass)) {
                try {
                    methodContainer.getMethodHandle().invoke(methodContainer.getInvoker(), packet);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * Sends a packet to the given channel.
     *
     * @param packet The packet that should be sent to the given channel.
     * @param channel The channel that will receive the packet.
     */
    public static void sendPacket(Packet packet, Channel channel) {
        if (channel.isOpen() && channel.isWritable()) {
            channel.writeAndFlush(packet);
        } else {
            throw new IllegalArgumentException("Unable to access the given channel");
        }
    }

    /**
     * Broadcasts a packet to all channels.
     *
     * @param packet    The packet that is broadcasted.
     */
    public static void broadcastPacket(Packet packet) {
        channels.writeAndFlush(packet);
    }

    /**
     * Broadcasts a packet to all matched channels.
     *
     * @param packet    The packet that is broadcasted.
     * @param matcher   The channel matcher.
     */
    public static void broadcastPacket(Packet packet, ChannelMatcher matcher) {
        channels.writeAndFlush(packet, matcher);
    }

}
