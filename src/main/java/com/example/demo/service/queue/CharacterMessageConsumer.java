package com.example.demo.service.queue;

import com.rabbitmq.client.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CharacterMessageConsumer implements CommandLineRunner {

    private final ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;

    private final DeliverCallback deliverCallback = (consumer, delivery) -> {
        String message = new String(delivery.getBody());
        System.out.printf("Received character message: \"%s\"%n", message);
    };

    @Override
    public void run(String... args) {
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

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
