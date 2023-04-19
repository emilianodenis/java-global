package com.example.demo.service.queue;

import com.example.demo.model.DemoMessage;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

@Service
public class MessagePublisher {

    private final ConnectionFactory factory = new ConnectionFactory();

    public <T> void publishSimpleMessage(Exchange exchange, Queue queue, AMQP.BasicProperties properties, DemoMessage<T> message) {

        try (var connection = factory.newConnection(); var channel = connection.createChannel(); var byteOutputStream = new ByteArrayOutputStream(); var objectOutputStream = new ObjectOutputStream(byteOutputStream)) {
            objectOutputStream.writeObject(message);
            channel.basicPublish(exchange.getExchange(), queue.getQueueType(), properties, byteOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
