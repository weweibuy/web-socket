package com.weweibuy.socket.ws.socketio.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * socket.iod 传输类型
 *
 * @author durenhao
 * @date 2021/12/17 19:50
 **/
@Getter
@RequiredArgsConstructor
public enum SocketIoTransportType {

    /**
     * 轮询
     */
    POLLING("polling"),

    /**
     * websocket
     */
    WEBSOCKET("websocket"),
    ;

    private final String transport;


    public static SocketIoTransportType fromTransport(String transport) {
        return Arrays.stream(SocketIoTransportType.values())
                .filter(s -> s.getTransport().equals(transport))
                .findFirst()
                .orElse(null);
    }

}
