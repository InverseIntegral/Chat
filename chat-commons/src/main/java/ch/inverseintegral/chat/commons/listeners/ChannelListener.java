package ch.inverseintegral.chat.commons.listeners;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A special type of listener that also passes the receiving channel.
 * A method annotated by this annoatation must have exactly two parameters.
 * 1. An instance of {@link ch.inverseintegral.chat.commons.packets.Packet packet} and
 * 2. an instance of {@link io.netty.channel.Channel channel}.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 * @see Listener
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ChannelListener {

}
