package ch.inverseintegral.chat.server;

import ch.inverseintegral.chat.commons.Bootstrap;
import ch.inverseintegral.chat.commons.Registry;
import ch.inverseintegral.chat.server.listeners.LoginListener;
import ch.inverseintegral.chat.server.listeners.StringBasedChatMessageListener;

/**
 * A WebSocketServer.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class WebSocketServer {

    public static void main(String[] args) {
        Registry.registerListener(new StringBasedChatMessageListener());
        Registry.registerListener(new LoginListener());

        Bootstrap.server();
    }

}
