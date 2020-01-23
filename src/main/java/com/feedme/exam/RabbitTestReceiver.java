package com.feedme.exam;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

public class RabbitTestReceiver implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitTestReceiver.class);

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
        SpringApplication.run(RabbitTestReceiver.class, args);
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
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            LOG.info(" [x] Received '{}'", message );
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
