package com.example.demo.service.queue;

import com.example.demo.entity.Profession;
import com.example.demo.model.DemoMessage;
import com.example.demo.websocket.SessionHandler;
import com.rabbitmq.client.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

@Component
public class ProfessionMessageConsumer implements CommandLineRunner {

    private final ConnectionFactory factory = new ConnectionFactory();
    private final SessionHandler wsSessionHandler;

    public ProfessionMessageConsumer(SessionHandler wsSessionHandler) {
        this.wsSessionHandler = wsSessionHandler;
    }

    private final DeliverCallback deliverCallback = (consumer, delivery) -> {
        processCallback(delivery);
    };

    private void processCallback(Delivery delivery) {
        try (var inputStream = new ByteArrayInputStream(delivery.getBody()); var outputStream = new ObjectInputStream(inputStream)) {
            var demoMessage = (DemoMessage<Profession>) outputStream.readObject();
            wsSessionHandler.notifyProfessionChange(demoMessage.toString());

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
                    Queue.PROFESSIONS.getQueueType(),
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
