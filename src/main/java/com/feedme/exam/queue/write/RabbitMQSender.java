package com.feedme.exam.queue.write;

import com.feedme.exam.queue.feed.FeedmeClient;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class RabbitMQSender implements CommandLineRunner  {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQSender.class);

    @Value("${feedme.host}")
    private String host;

    @Value("${feedme.feed_port}")
    private Integer feed_port;

    @Value("${feedme.types_port}")
    private Integer types_port;

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
        SpringApplication.run(RabbitMQSender.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        FeedmeClient feedme = new FeedmeClient();
        feedme.startConnection(this.host, this.feed_port, this.types_port);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.queueHost);
        factory.setUsername(this.queueUsername);
        factory.setPassword(this.queuePassword);
        factory.setVirtualHost(this.queueVhost);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(this.queueName, false, false, false, null);
            String message;

            while ((message = feedme.getNextJson()) != null) {
                channel.basicPublish("", this.queueName, null, message.getBytes(StandardCharsets.UTF_8));
                LOG.info("Sent '" + message + "'");
            }
            feedme.stopConnection();
        }
    }
}