package com.weweibuy.socket.ws.socketio.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Socket.IO 消息类型
 *
 * @author durenhao
 * @date 2021/12/19 20:24
 **/
@Getter
@RequiredArgsConstructor
public enum SioMessageType {

    CONNECT(0),

    DISCONNECT(1),

    EVENT(2),

    ACK(3),

    ERROR(4),

    BINARY_EVENT(5),

    BINARY_ACK(6),
    ;


    private final Integer code;


}
