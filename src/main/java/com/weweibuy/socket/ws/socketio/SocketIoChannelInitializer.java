package com.weweibuy.socket.ws.socketio;

import com.weweibuy.socket.ws.socketio.handler.WebSocketHandler;
import com.weweibuy.socket.ws.socketio.handler.PollingHandler;
import com.weweibuy.socket.ws.socketio.socket.SocketIoWebSocketFrameHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketDecoderConfig;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolConfig;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 初始化
 *
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
                // 最大长度
                .maxFramePayloadLength(Integer.MAX_VALUE)
                .build();

        WebSocketHandler socketIoHandshakeWebSocketServerProtocolHandler =
                new WebSocketHandler(config);

        ch.pipeline()
                .addLast(HttpServerCodec.class.getName(), new HttpServerCodec())
                .addLast(HttpObjectAggregator.class.getName(), new HttpObjectAggregator(8192))
                .addLast(ChunkedWriteHandler.class.getName(), new ChunkedWriteHandler())
                .addLast(PollingHandler.class.getName(), new PollingHandler())
                .addLast(WebSocketHandler.class.getName(), socketIoHandshakeWebSocketServerProtocolHandler)
                .addLast(SocketIoWebSocketFrameHandler.class.getName(), new SocketIoWebSocketFrameHandler());

    }
}
