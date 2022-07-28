package com.weweibuy.socket.ws.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author durenhao
 * @date 2021/12/12 16:24
 **/
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
        pipeline.addLast(new HttpServerCodec());
        //netty是基于分段请求的，HttpObjectAggregator的作用是将请求分段再聚合,参数是聚合字节的最大长度
        pipeline.addLast(new HttpObjectAggregator(8192));

        //以块的方式来写的处理器
        pipeline.addLast(new ChunkedWriteHandler());

        pipeline.addLast(new WebSocketServerProtocolHandler("/", true));

        //参数指的是contex_path
        //websocket定义了传递数据的6中frame类型
        pipeline.addLast(new TextWebSocketFrameHandler());
    }
}
