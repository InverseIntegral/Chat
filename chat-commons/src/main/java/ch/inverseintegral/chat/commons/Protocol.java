package ch.inverseintegral.chat.commons;

import ch.inverseintegral.chat.commons.packets.base.StringBasedChatMessagePacket;
import ch.inverseintegral.chat.commons.packets.init.LoginPacket;
import ch.inverseintegral.chat.commons.packets.Packet;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The protocol defines ids to the corresponding packets.
 * This allows the encoding and decoding of packets using ids.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public enum Protocol {

    LOGIN(0, LoginPacket.class),
    STRING_MESSAGE(1, StringBasedChatMessagePacket.class);
    /*TEA_POT(256, Teapot.class); Currently disabled */

    /**
     * A map of ids and packets.
     */
    public static final Map<Integer, Class<? extends Packet>> ID_TO_CLASS = new HashMap<>(Protocol.values().length);

    /**
     * A map of packets and ids.
     */
    public static final Map<Class<? extends Packet>, Integer> CLASS_TO_ID = new HashMap<>(Protocol.values().length);

    /**
     * The unique identifier of this packet.
     */
    private final int id;

    /**
     * The class that implements this packet.
     */
    private final Class<? extends Packet> packetClass;

    Protocol(int id, Class<? extends Packet> packetClass) {
        this.id = id;
        this.packetClass = packetClass;
    }

    static {
        // Creates a map entry for every packet in this protocol.
        for (Protocol protocol : Arrays.asList(values())) {
            ID_TO_CLASS.put(protocol.id, protocol.packetClass);
            CLASS_TO_ID.put(protocol.packetClass, protocol.id);
        }
    }

    /**
     * Gets a packet implementation by its id.
     *
     * @param id The id of the packet.
     * @return Returns the class that implements this packet or null if there is no implementation.
     */
    public static Class<? extends Packet> getClass(int id) {
        return ID_TO_CLASS.get(id);
    }

    /**
     * Gets a packet implementation by reading its id from the byte buffer.
     *
     * @param byteBuf The byte buffer that contains an integer at the {@link ByteBuf#readerIndex() reader index}.
     * @return Returns the class that implements this packet or null if there is no implementation.
     */
    public static Class<? extends Packet> getClass(ByteBuf byteBuf) {
        return getClass(byteBuf.readInt());
    }

    /**
     * Gets the unique identifier from a packet implementation class.
     *
     * @param packetClass The class of the packet.
     * @return Returns the id of this specific packet in the protocol.
     */
    public static Integer getId(Class<? extends Packet> packetClass) {
        return CLASS_TO_ID.get(packetClass);
    }

}
