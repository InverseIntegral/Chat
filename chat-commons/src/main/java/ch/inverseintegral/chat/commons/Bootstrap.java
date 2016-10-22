package ch.inverseintegral.chat.commons;

import ch.inverseintegral.chat.commons.handlers.WebSocketHandshakeHandler;
import ch.inverseintegral.chat.commons.handlers.PacketHandler;
import ch.inverseintegral.chat.commons.handlers.WebSocketConvertHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LoggingHandler;

import java.net.URI;

/**
 * A Bootstrap.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public final class Bootstrap {

    public static Channel client(URI uri, WebSocketHandshakeHandler handshakeHandler) {
        EventLoopGroup group = new NioEventLoopGroup();
        ClientChannelInitializer channelInitializer = new ClientChannelInitializer(handshakeHandler);

        try {
            io.netty.bootstrap.Bootstrap bootstrap = new io.netty.bootstrap.Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(channelInitializer);

            Channel channel = bootstrap.connect(uri.getHost(), 1337).sync().channel();
            handshakeHandler.handshakeFuture().sync();

            channel.pipeline().remove(handshakeHandler);
            channel.pipeline().addLast(new WebSocketConvertHandler(), new PacketHandler());

            return channel;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void server() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler())
                    .childHandler(new ServerChannelInitializer());

            Channel channel = serverBootstrap.bind(1337).sync().channel();
            channel.closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    static class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

        private WebSocketHandshakeHandler handshakeHandler;

        public ClientChannelInitializer(WebSocketHandshakeHandler handshakeHandler) {
            this.handshakeHandler = handshakeHandler;
        }

        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
            channel.pipeline().addLast(new HttpClientCodec(),
                    new HttpObjectAggregator(8192),
                    handshakeHandler);
        }
    }

    static class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

        private static final String WEBSOCKET_PATH = "/websocket";

        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
            channel.pipeline().addLast(new HttpServerCodec(),
                    new HttpObjectAggregator(65536),
                    new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true),
                    new WebSocketConvertHandler(),
                    new PacketHandler());
        }
    }

}
