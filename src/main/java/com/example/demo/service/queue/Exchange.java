package com.example.demo.service.queue;

public enum Exchange {
    DEFAULT("");

    private final String exchange;

    Exchange(String exchange) {
        this.exchange = exchange;
    }

    public String getExchange() {
        return exchange;
    }
}