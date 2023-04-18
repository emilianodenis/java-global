package com.example.demo.service.queue;

import com.rabbitmq.client.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CharacterMessageConsumer implements CommandLineRunner {

    private final ConnectionFactory factory = new ConnectionFactory();

    private final DeliverCallback deliverCallback = (consumer, delivery) -> {
        String message = new String(delivery.getBody());
        System.out.printf("Received character message: \"%s\"%n", message);
    };

    @Override
    public void run(String... args) {
        try {
            var connection = factory.newConnection();
            var channel = connection.createChannel();

            channel.basicConsume(
                    Queue.CHARACTERS.getQueueType(),
                    true,
                    deliverCallback,
                    consumerTag -> {
                    }
            );
        } catch (Exception e) {
        }
    }
}
