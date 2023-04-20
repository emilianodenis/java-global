package com.example.demo.service.queue;

import com.example.demo.utils.ObjectUtils;
import com.example.demo.websocket.ProfessionSessionHandler;
import com.rabbitmq.client.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProfessionMessageConsumer implements CommandLineRunner {

    private final ConnectionFactory factory = new ConnectionFactory();
    private final ProfessionSessionHandler wsSessionHandler;

    public ProfessionMessageConsumer(ProfessionSessionHandler wsSessionHandler) {
        this.wsSessionHandler = wsSessionHandler;
    }

    private final DeliverCallback deliverCallback = (consumer, delivery) -> {
        processCallback(delivery);
    };

    private void processCallback(Delivery delivery) {
        try {
            wsSessionHandler.notifyProfessionChange(ObjectUtils.deSerialize(delivery.getBody()));
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
