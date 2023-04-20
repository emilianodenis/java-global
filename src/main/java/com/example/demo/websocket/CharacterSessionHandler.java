package com.example.demo.websocket;

import com.example.demo.entity.Character;
import com.example.demo.model.Action;
import com.example.demo.model.DemoMessage;
import com.example.demo.utils.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CharacterSessionHandler extends TextWebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessions.add(session);
        var message = new DemoMessage<Character>(Action.SESSION_ID, null, null, session.getId());
        session.sendMessage(new TextMessage(ObjectUtils.toJsonString(message)));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessions.remove(session);
    }

    private final List<WebSocketSession> sessions = new ArrayList<>();

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    private void sendMessage(DemoMessage<Character> message) {
        for (var session : sessions) {
            try {
                session.sendMessage(new TextMessage(ObjectUtils.toJsonString(message)));
            } catch (IOException e) {
                removeSession(session);
            }
        }
    }

    public void notifyCharacterChange(DemoMessage<Character> message) {
        sendMessage(message);
    }
}
