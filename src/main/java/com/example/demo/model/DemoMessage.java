package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DemoMessage<T> implements Serializable {

    private final Action action;
    private final T message;
    private final Integer id;
    private final String sessionId;

    public DemoMessage(Action action, T message, Integer id, String sessionId) {
        this.action = action;
        this.message = message;
        this.id = id;
        this.sessionId = sessionId;
    }

    public Action getAction() {
        return action;
    }

    public T getMessage() {
        return message;
    }

    public Integer getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }

    @Override
    public String toString() {
        return "DemoMessage{" +
                "action=" + action +
                ",id=" + id +
                ", message=" + message +
                ", sessionId=" + sessionId +
                '}';
    }
}
