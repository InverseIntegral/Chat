package ch.inverseintegral.chat.client.listeners;

import ch.inverseintegral.chat.commons.listeners.Listener;
import ch.inverseintegral.chat.commons.packets.init.LoginPacket;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class LoginListener {

    @Listener
    public void onLogin(LoginPacket loginPacket) {
        System.out.println(loginPacket.getUsername() + " connected to the chat room");
    }

}
