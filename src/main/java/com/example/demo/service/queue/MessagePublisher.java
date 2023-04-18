package com.example.demo.service.queue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Service;

@Service
public class MessagePublisher {

    private final ConnectionFactory factory = new ConnectionFactory();

    public void publishSimpleString(Exchange exchange, Queue queue, AMQP.BasicProperties properties, String message) {

        try (var connection = factory.newConnection(); var channel = connection.createChannel()) {
            channel.basicPublish(exchange.getExchange(), queue.getQueueType(), properties, message.getBytes());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
