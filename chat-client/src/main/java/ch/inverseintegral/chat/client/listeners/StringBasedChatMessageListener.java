package ch.inverseintegral.chat.client.listeners;

import ch.inverseintegral.chat.commons.Listener;
import ch.inverseintegral.chat.commons.packets.base.StringBasedChatMessagePacket;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class StringBasedChatMessageListener {

    @Listener
    public void onStringMessage(StringBasedChatMessagePacket packet) {
        System.out.println(packet.getMessage());
    }

}
