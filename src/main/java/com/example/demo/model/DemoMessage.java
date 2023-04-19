package com.example.demo.model;

import java.io.Serializable;

public class DemoMessage<T>  implements Serializable {

    private final Action action;
    private final T message;

    private final  Integer id;

    public DemoMessage(Action action, T message, Integer id) {
        this.action = action;
        this.message = message;
        this.id = id;
    }

    public Action getAction() {
        return action;
    }

    public T getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "DemoMessage{" +
                "action=" + action +
                ",id=" + id +
                ", message=" + message +
                '}';
    }
}
