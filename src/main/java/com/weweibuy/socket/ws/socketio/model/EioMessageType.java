package com.weweibuy.socket.ws.socketio.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Engine.IO 消息类型
 * <p>
 * Engine.IO是socket.io 的底层
 *
 * @author durenhao
 * @date 2021/12/17 20:10
 **/
@Getter
@RequiredArgsConstructor
public enum EioMessageType {

    OPEN(0),

    CLOSE(1),

    PING(2),

    PONG(3),

    MESSAGE(4),

    UPGRADE(5),

    NOOP(6),
    ;

    private final Integer code;


}
