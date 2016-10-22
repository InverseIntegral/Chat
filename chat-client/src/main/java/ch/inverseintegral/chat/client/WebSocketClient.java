package ch.inverseintegral.chat.client;

import ch.inverseintegral.chat.client.listeners.StringBasedChatMessageListener;
import ch.inverseintegral.chat.commons.Bootstrap;
import ch.inverseintegral.chat.commons.Registry;
import ch.inverseintegral.chat.commons.handlers.PacketHandler;
import ch.inverseintegral.chat.commons.handlers.WebSocketHandshakeHandler;
import ch.inverseintegral.chat.commons.packets.base.StringBasedChatMessagePacket;
import ch.inverseintegral.chat.commons.packets.init.LoginPacket;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A WebSocketClient initializes the connection to a websocket.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class WebSocketClient {

    public static void main(String[] args) throws Exception {

        // Register listeners.
        Registry.registerListener(new StringBasedChatMessageListener());

        // Create the handshaker abstraction and the uri.
        URI uri = new URI("ws://127.0.0.1:1337/websocket");
        final WebSocketHandshakeHandler handler = new WebSocketHandshakeHandler(WebSocketClientHandshakerFactory.newHandshaker(uri,
                WebSocketVersion.V13,
                null,
                false,
                new DefaultHttpHeaders()));

        // Client bootstrap...
        Channel channel = Bootstrap.client(uri, handler);
        System.out.println("Enter your username: ");

        try (Scanner scanner = new Scanner(System.in)) {

            String username = scanner.nextLine();
            PacketHandler.sendPacket(new LoginPacket(username), channel);

            while (true) {

                try {
                    String msg = scanner.nextLine();
                    PacketHandler.sendPacket(new StringBasedChatMessagePacket(msg), channel);
                } catch (NoSuchElementException | IllegalStateException e) {
                    break;
                }

            }

        }
    }

}
