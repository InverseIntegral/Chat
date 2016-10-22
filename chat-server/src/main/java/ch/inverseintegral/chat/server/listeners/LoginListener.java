package ch.inverseintegral.chat.server.listeners;

import ch.inverseintegral.chat.commons.handlers.PacketHandler;
import ch.inverseintegral.chat.commons.Listener;
import ch.inverseintegral.chat.commons.packets.base.StringBasedChatMessagePacket;
import ch.inverseintegral.chat.commons.packets.init.LoginPacket;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class LoginListener {

    @Listener
    public void onLogin(LoginPacket loginPacket) {
        StringBasedChatMessagePacket loginMessage = new StringBasedChatMessagePacket(loginPacket.getUsername() + " connected");
        PacketHandler.broadcastPacket(loginMessage);
    }

}
