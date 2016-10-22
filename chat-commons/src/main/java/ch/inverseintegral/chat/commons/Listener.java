package ch.inverseintegral.chat.commons;

import ch.inverseintegral.chat.commons.packets.Packet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A Listener listens on a packet received event.
 * A method that is annotated with {@code @Listener} must have exactly two parameter.
 * The first parameter must be a subclass of {@link Packet packet} and
 * the second Parameter is a {@link io.netty.channel.Channel channel}.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Listener {

}
