package ch.inverseintegral.chat.commons.handlers;

import ch.inverseintegral.chat.commons.Protocol;
import ch.inverseintegral.chat.commons.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

/**
 * A WebSocketConvertHandler converts incoming and outgoing messages.
 * Incoming and outgoing messages are {@link BinaryWebSocketFrame binary websocket frames}
 * they have to be converted to {@link Packet packets} for internal use.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class WebSocketConvertHandler extends MessageToMessageCodec<BinaryWebSocketFrame, Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> out) throws Exception {
        ByteBuf byteBuf = Unpooled.buffer();

        // Write the id of the packet and the packet itself.
        byteBuf.writeInt(Protocol.getId(packet.getClass()));
        packet.write(byteBuf);

        out.add(new BinaryWebSocketFrame(byteBuf));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame message, List<Object> out) throws Exception {
        ByteBuf byteBuf = message.content();

        // Get the packet, instantiate it and read the data from the byte buffer.
        Class<? extends Packet> packetClass = Protocol.getClass(byteBuf);
        Packet packet = packetClass.newInstance();
        packet.read(byteBuf);

        out.add(packet);
    }

}
