package com.weweibuy.socket.ws.socketio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author durenhao
 * @date 2021/12/16 22:06
 **/
public class SocketIoServer {

    private NioEventLoopGroup boss;

    private NioEventLoopGroup worker;

    public void start(SocketIoServerConfig config) {
        ChannelFuture channelFuture = nettyServer(config);
        channelFuture.syncUninterruptibly();
    }

    public static void main(String[] args) {
        SocketIoServer socketIoServer = new SocketIoServer();
        SocketIoServerConfig socketIoServerConfig = new SocketIoServerConfig();
        socketIoServerConfig.setPort(9092);
        socketIoServer.start(socketIoServerConfig);
    }

    private ChannelFuture nettyServer(SocketIoServerConfig config) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        boss = new NioEventLoopGroup(config.getBossThreadNum());
        worker = new NioEventLoopGroup(config.getWorkThreadNum());
        return serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new SocketIoChannelInitializer())
                .bind(config.getPort());
    }

    public void stop() {
        boss.shutdownGracefully().syncUninterruptibly();
        worker.shutdownGracefully().syncUninterruptibly();
    }


}
