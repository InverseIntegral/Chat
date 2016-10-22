package ch.inverseintegral.chat.server.listeners;

import ch.inverseintegral.chat.commons.Listener;
import ch.inverseintegral.chat.commons.handlers.PacketHandler;
import ch.inverseintegral.chat.commons.packets.base.StringBasedChatMessagePacket;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelMatchers;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class StringBasedChatMessageListener {

    @Listener
    public void onStringMessage(StringBasedChatMessagePacket packet, Channel channel) {
        PacketHandler.broadcastPacket(packet, ChannelMatchers.isNot(channel));
    }

}
