package ch.inverseintegral.chat.commons.packets.base;

import ch.inverseintegral.chat.commons.packets.Packet;
import io.netty.buffer.ByteBuf;

/**
 * A StringBasedChatMessagePacket represents a chat message that consists of a message.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class StringBasedChatMessagePacket extends Packet {

    /**
     * The text message.
     */
    private String message;

    /**
     * Used for reflection.
     */
    public StringBasedChatMessagePacket() {
    }

    public StringBasedChatMessagePacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(this.message, byteBuf);
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.message = readString(byteBuf);
    }

}
