package ch.inverseintegral.chat.server;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public final class UserContainer {

    private static final Map<Channel, String> usernames = new HashMap<>();

    public static void addUsername(String username, Channel channel) {
        usernames.put(channel, username);

        channel.closeFuture().addListener(future -> usernames.remove(channel));
    }

    public static String getUsername(Channel channel) {
        return usernames.get(channel);
    }

    public static boolean containsChannel(Channel channel) {
        return usernames.containsKey(channel);
    }

    private UserContainer() {}

}
