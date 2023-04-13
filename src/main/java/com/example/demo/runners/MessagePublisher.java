package com.example.demo.runners;

import com.example.demo.DemoApplication;
import com.example.demo.message.Receiver;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.IntStream;

@Component
public class MessagePublisher implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public MessagePublisher(RabbitTemplate rabbitTemplate, Receiver receiver) {
        this.rabbitTemplate = rabbitTemplate;
        this.receiver = receiver;
    }

    @Override
    public void run(String... args) {
        System.out.println("starting messages");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        var ints = IntStream.rangeClosed(1, 10).limit(100).toArray();
        var routingKey = DemoApplication.baseRoutingKey + "baz";
        var topicExchangeName = DemoApplication.topicExchangeName;

        Arrays
                .stream(ints)
                .parallel()
                .forEach(i -> {
                    var message = "Hello from RabbitMQ: " + i;
                    System.out.println(message);
                    rabbitTemplate.convertAndSend(topicExchangeName, routingKey, message);
                });
        rabbitTemplate.convertAndSend(topicExchangeName, routingKey, 152);
        rabbitTemplate.convertAndSend(topicExchangeName, routingKey, "Hello from RabbitMQ: FINAL");


        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("ending messages");
    }
}
