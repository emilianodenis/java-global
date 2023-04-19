package com.example.demo.service.queue;

import com.example.demo.websocket.SessionHandler;
import com.rabbitmq.client.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProfessionMessageConsumer implements CommandLineRunner {

    private final ConnectionFactory factory = new ConnectionFactory();
    private final SessionHandler wsSessionHandler;

    public ProfessionMessageConsumer(SessionHandler wsSessionHandler) {
        this.wsSessionHandler = wsSessionHandler;
    }

    private final DeliverCallback deliverCallback = (consumer, delivery) -> processCallback(delivery);

    private void processCallback(Delivery delivery) {
        String message = new String(delivery.getBody());
        wsSessionHandler.notifyProfessionChange(message);
    }

    @Override
    public void run(String... args) {
        try {
            var connection = factory.newConnection();
            var channel = connection.createChannel();

            channel.basicConsume(
                    Queue.PROFESSIONS.getQueueType(),
                    true,
                    deliverCallback,
                    consumerTag -> {
                    }
            );
        } catch (Exception e) {
        }
    }
}
