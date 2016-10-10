package ch.inverseintegral.chat.commons.packets;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A Packet defines how specific data is written to a buffer as bytes.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public abstract class Packet {

    /**
     * Specifies how to write the packet to the buffer.
     *
     * @param byteBuf The byte buffer.
     */
    public abstract void write(ByteBuf byteBuf);

    /**
     * Specifies how to read the packet from the buffer.
     *
     * @param byteBuf The byte buffer.
     */
    public abstract void read(ByteBuf byteBuf);

    /**
     * Writes a string to the byte buffer.
     *
     * @param message The message that is written to the byte buffer.
     * @param byteBuf The byte buffer.
     */
    protected void writeString(String message, ByteBuf byteBuf) {
        byte[] byteMessage = message.getBytes(StandardCharsets.UTF_8);

        byteBuf.writeInt(byteMessage.length);
        byteBuf.writeBytes(byteMessage);
    }

    /**
     * Reads a string from the byte buffer.
     *
     * @param byteBuf The byte buffer that will contain a string at the {@link ByteBuf#readerIndex() reader Index}.
     * @return Returns the read string.
     */
    protected String readString(ByteBuf byteBuf) {
        int length = byteBuf.readInt();
        byte[] byteMessage = new byte[length];

        byteBuf.readBytes(byteMessage);
        return new String(byteMessage, StandardCharsets.UTF_8);
    }

}
