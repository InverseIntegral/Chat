package ch.inverseintegral.chat.server.listeners;

import ch.inverseintegral.chat.commons.handlers.PacketHandler;
import ch.inverseintegral.chat.commons.listeners.ChannelListener;
import ch.inverseintegral.chat.commons.packets.base.StringBasedChatMessagePacket;
import ch.inverseintegral.chat.server.UserContainer;
import io.netty.channel.Channel;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class StringBasedChatMessageListener {

    @ChannelListener
    public void onStringMessage(StringBasedChatMessagePacket packet, Channel channel) {
        if (!UserContainer.containsChannel(channel)) {
            channel.close();
            return;
        }

        packet = new StringBasedChatMessagePacket(UserContainer.getUsername(channel) + "> " + packet.getMessage());
        PacketHandler.broadcastPacket(packet);
    }

}
