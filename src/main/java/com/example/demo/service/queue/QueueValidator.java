package com.example.demo.service.queue;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class QueueValidator implements CommandLineRunner {


    @Override
    public void run(String... args) {
        var factory = new ConnectionFactory();

        for (var queue : Queue.values()) {
            validateQueue(factory, queue);
        }
    }

    private void validateQueue(ConnectionFactory factory, Queue queue) {
        if (!checkQueueExists(factory, queue)) {
            createQueue(factory, queue);
        }
    }

    private boolean checkQueueExists(ConnectionFactory factory, Queue queue) {
        try (var connection = factory.newConnection(); var channel = connection.createChannel()) {
            var isOk = channel.queueDeclarePassive(queue.getQueueType());
            return isOk != null;
        } catch (IOException | TimeoutException e) {
            return false;
        }
    }

    private void createQueue(ConnectionFactory factory, Queue queue) {
        try (var connection = factory.newConnection(); var channel = connection.createChannel()) {
            channel.queueDeclare(queue.getQueueType(), true, false, false, null);
        } catch (IOException | TimeoutException e) {
            System.out.println(e.getMessage());
        }
    }
}
