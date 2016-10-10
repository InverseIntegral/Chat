package ch.inverseintegral.chat.commons.packets.init;

import ch.inverseintegral.chat.commons.packets.Packet;
import io.netty.buffer.ByteBuf;

/**
 * A LoginPacket symbolizes a login using a username.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class LoginPacket extends Packet {

    /**
     * The username of the new user.
     */
    private String username;

    /**
     * Used for reflection.
     */
    public LoginPacket() {
    }

    public LoginPacket(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        writeString(this.username, byteBuf);
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.username = readString(byteBuf);
    }

}
