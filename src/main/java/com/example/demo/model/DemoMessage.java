package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DemoMessage<T> implements Serializable {

    private final Action action;
    private final T content;
    private final Integer id;
    private final String sessionId;

    public DemoMessage(Action action, T content, Integer id, String sessionId) {
        this.action = action;
        this.content = content;
        this.id = id;
        this.sessionId = sessionId;
    }

    public Action getAction() {
        return action;
    }

    public T getContent() {
        return content;
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
                ", message=" + content +
                ", sessionId=" + sessionId +
                '}';
    }
}
