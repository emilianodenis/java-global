package com.example.demo.service.queue;

import com.example.demo.utils.ObjectUtils;
import com.example.demo.websocket.CharacterSessionHandler;
import com.rabbitmq.client.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class CharacterMessageConsumer implements CommandLineRunner {

    private final ConnectionFactory factory = new ConnectionFactory();
    private final CharacterSessionHandler wsSessionHandler;

    public CharacterMessageConsumer(CharacterSessionHandler wsSessionHandler) {
        this.wsSessionHandler = wsSessionHandler;
    }

    private final DeliverCallback deliverCallback = (consumer, delivery) -> {
        processCallback(delivery);
    };

    private void processCallback(Delivery delivery) {
        try {
            wsSessionHandler.notifyCharacterChange(ObjectUtils.deSerialize(delivery.getBody()));
        } catch (Exception e) {
            System.out.println("cannot process: " + e.getMessage());
        }
    }

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
            System.out.println(e.getMessage());
        }
    }
}
