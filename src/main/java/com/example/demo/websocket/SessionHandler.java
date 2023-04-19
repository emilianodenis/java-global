package com.example.demo.websocket;

import com.example.demo.entity.Profession;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SessionHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessions.add(session);
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

    private void sendMessage(JSONObject message) {
        for (var session : sessions) {
            try {
                session.sendMessage(new TextMessage(message.toString()));
            } catch (IOException e) {
                removeSession(session);
            }
        }
    }

    private void sendMessage(String message) {
        for (var session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                removeSession(session);
            }
        }
    }

//    public void notifyProfessionChange(Profession profession) {
//        try {
//            var pro = new ObjectMapper().writeValueAsString(profession);
//            var json = new JSONObject(pro);
//            sendMessage(json);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void notifyProfessionChange(String message) {
        sendMessage(message);
    }

    public void notifyCharacterChange(String message) {
        sendMessage(message);
    }
}
