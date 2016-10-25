package ch.inverseintegral.chat.server.listeners;

import ch.inverseintegral.chat.commons.handlers.PacketHandler;
import ch.inverseintegral.chat.commons.listeners.ChannelListener;
import ch.inverseintegral.chat.commons.packets.init.LoginPacket;
import ch.inverseintegral.chat.server.UserContainer;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelMatchers;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class LoginListener {

    @ChannelListener
    public void onLogin(LoginPacket loginPacket, Channel channel) {
        if (UserContainer.containsChannel(channel)) {
            channel.close();
            return;
        }

        UserContainer.addUsername(loginPacket.getUsername(), channel);
        PacketHandler.broadcastPacket(loginPacket, ChannelMatchers.isNot(channel));
    }

}
