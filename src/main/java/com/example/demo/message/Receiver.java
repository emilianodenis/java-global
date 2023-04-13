package com.example.demo.message;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {

    private final CountDownLatch latch = new CountDownLatch(1);

    public void receiverMessage(String message) {
        System.out.println("Received <" + message + ">");
        latch.countDown();
    }

    public void receiverMessage(int message) {
        System.out.println("Received number <" + message + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
