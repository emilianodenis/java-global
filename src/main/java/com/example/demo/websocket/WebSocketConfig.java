package com.example.demo.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ProfessionSessionHandler professionSessionHandler;
    private final CharacterSessionHandler characterSessionHandler;

    public WebSocketConfig(ProfessionSessionHandler session, CharacterSessionHandler characterSessionHandler) {
        this.professionSessionHandler = session;
        this.characterSessionHandler = characterSessionHandler;
    }

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(professionSessionHandler, "/ws/professions");
        registry.addHandler(characterSessionHandler, "/ws/characters");
    }
}
