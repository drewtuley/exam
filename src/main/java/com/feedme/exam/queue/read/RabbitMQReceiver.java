package com.feedme.exam.queue.read;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import java.nio.charset.StandardCharsets;

public class RabbitMQReceiver implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQReceiver.class);

    @Value("${queue.host}")
    private String queueHost;

    @Value("${queue.vhost}")
    private String queueVhost;

    @Value("${queue.username}")
    private String queueUsername;

    @Value("${queue.password}")
    private String queuePassword;

    @Value("${queue.name}")
    private String queueName;

    public static void main(String[] args) {
        SpringApplication.run(RabbitMQReceiver.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.queueHost);
        factory.setUsername(this.queueUsername);
        factory.setPassword(this.queuePassword);
        factory.setVirtualHost(this.queueVhost);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(queueName, false, false, false, null);
        LOG.info("Waiting for messages.");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            LOG.info("Received '{}'", message );
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
