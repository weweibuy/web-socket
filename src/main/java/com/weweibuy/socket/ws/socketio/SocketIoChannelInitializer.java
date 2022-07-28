package com.weweibuy.socket.ws.socketio;

import com.weweibuy.socket.ws.socketio.handshake.SocketIoHandshakeWebSocketServerProtocolHandler;
import com.weweibuy.socket.ws.socketio.socket.SocketIoPollReqHandler;
import com.weweibuy.socket.ws.socketio.socket.SocketIoWebSocketFrameHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketDecoderConfig;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolConfig;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author durenhao
 * @date 2021/12/16 22:13
 **/
public class SocketIoChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        WebSocketDecoderConfig socketDecoderConfig = WebSocketDecoderConfig.newBuilder()
                .maxFramePayloadLength(65536)
                .allowMaskMismatch(false)
                .allowExtensions(false)
                .withUTF8Validator(true)
                .closeOnProtocolViolation(true)
                .expectMaskedFrames(true)
                .build();

        WebSocketServerProtocolConfig config = WebSocketServerProtocolConfig.newBuilder()
                .websocketPath("/")
                .checkStartsWith(true)
                .subprotocols(null)
                .dropPongFrames(true)
                .forceCloseTimeoutMillis(0L)
                .handshakeTimeoutMillis(10000L)
                .decoderConfig(socketDecoderConfig)
                .build();

        SocketIoHandshakeWebSocketServerProtocolHandler socketIoHandshakeWebSocketServerProtocolHandler =
                new SocketIoHandshakeWebSocketServerProtocolHandler(config);

        ch.pipeline()
                .addLast(HttpServerCodec.class.getName(), new HttpServerCodec())
                .addLast(HttpObjectAggregator.class.getName(), new HttpObjectAggregator(8192))
                .addLast(ChunkedWriteHandler.class.getName(), new ChunkedWriteHandler())
                .addLast(SocketIoHandshakeWebSocketServerProtocolHandler.class.getName(), socketIoHandshakeWebSocketServerProtocolHandler)
                .addLast(SocketIoPollReqHandler.class.getName(), new SocketIoPollReqHandler())
                .addLast(SocketIoWebSocketFrameHandler.class.getName(), new SocketIoWebSocketFrameHandler());

    }
}
