package com.weweibuy.socket.ws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author durenhao
 * @date 2021/12/11 22:00
 **/
@Configuration
public class SocketConfig {


    /**
     * 初始化Bean，它会自动注册使用了 @ServerEndpoint 注解声明的 WebSocket endpoint
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
