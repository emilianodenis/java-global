package com.example.demo.service.queue;

import com.example.demo.model.DemoMessage;
import com.example.demo.utils.ObjectUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Service;

@Service
public class MessagePublisher {

    private final ConnectionFactory factory = new ConnectionFactory();

    public <T> void publishSimpleMessage(Exchange exchange, Queue queue, AMQP.BasicProperties properties, DemoMessage<T> message) {
        try (var connection = factory.newConnection(); var channel = connection.createChannel()) {
            channel.basicPublish(exchange.getExchange(), queue.getQueueType(), properties, ObjectUtils.serialize(message));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
