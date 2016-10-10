package ch.inverseintegral.chat.commons.handlers;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;

/**
 * A WebSocketHandshakeHandler handles the initializing steps for a
 * websocket connection on the client side.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class WebSocketHandshakeHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    /**
     * Client side handshaker abstraction.
     */
    private final WebSocketClientHandshaker handshaker;

    /**
     * Handshake future that will return once the handshake
     * bas been completed.
     */
    private ChannelPromise handshakeFuture;

    public WebSocketHandshakeHandler(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    /**
     * Gets the handshake future that will return once
     * the handshake has been completed.
     *
     * @return Returns the handshake future.
     */
    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        handshaker.handshake(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        if(handshaker.isHandshakeComplete()) {
            throw new IllegalStateException("Unexpected FullHttpResponse " + msg);
        } else {
            handshaker.finishHandshake(ctx.channel(), msg);
            handshakeFuture.setSuccess();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();

        if(!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }

        ctx.close();
    }

}
