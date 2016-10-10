package ch.inverseintegral.chat.server.listeners;

import ch.inverseintegral.chat.commons.handlers.PacketHandler;
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
        PacketHandler.broadcastPacket(packet);
    }

}
