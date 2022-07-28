package com.weweibuy.socket.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * @author durenhao
 * @date 2021/12/11 21:47
 **/
@SpringBootApplication
@EnableWebSocket
public class WsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WsApplication.class, args);
    }

}
