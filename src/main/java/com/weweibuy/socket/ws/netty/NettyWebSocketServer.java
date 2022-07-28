package com.weweibuy.socket.ws.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author durenhao
 * @date 2021/12/12 16:12
 **/
public class NettyWebSocketServer {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossExecutors = new NioEventLoopGroup();
        NioEventLoopGroup workerExecutors = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture channelFuture = serverBootstrap.group(bossExecutors, workerExecutors)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new WebSocketChannelInitializer())
                .bind(new InetSocketAddress(9092));
        ChannelFuture sync = channelFuture.sync();

    }



}
