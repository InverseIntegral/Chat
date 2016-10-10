package ch.inverseintegral.chat.commons.packets.base;

import ch.inverseintegral.chat.commons.packets.Packet;
import io.netty.buffer.ByteBuf;

/**
 * A Teapot.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class Teapot extends Packet {

    private int answer = 42;
    private String text = "Test";

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(text, byteBuf);
        byteBuf.writeInt(answer);
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.text = readString(byteBuf);
        this.answer = byteBuf.readInt();
    }

}
