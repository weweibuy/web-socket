package com.weweibuy.socket.ws.socketio;

import org.junit.Test;

public class SocketIoServerTest {

    @Test
    public void start() throws Exception {
        SocketIoServer socketIoServer = new SocketIoServer();
        SocketIoServerConfig socketIoServerConfig = new SocketIoServerConfig();
        socketIoServerConfig.setPort(9092);
        socketIoServer.start(socketIoServerConfig);
        Thread.sleep(Integer.MAX_VALUE);
    }
}