package com.example.demo.service.queue;

public enum Queue {
    CHARACTERS("characters"),
    PROFESSIONS("professions");

    private final String queueType;

    Queue(String queueType) {
        this.queueType = queueType;
    }

    public String getQueueType() {
        return queueType;
    }
}