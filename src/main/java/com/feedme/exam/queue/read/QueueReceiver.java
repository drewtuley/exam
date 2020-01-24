package com.feedme.exam.queue.read;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class QueueReceiver implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(QueueReceiver.class);

    @Value("${queue.host}")
    private String queueHost;

    @Value("${queue.vhost}")
    private String queueVhost;

    @Value("${queue.read.username}")
    private String queueUsername;

    @Value("${queue.read.password}")
    private String queuePassword;

    @Value("${queue.name}")
    private String queueName;

    @Autowired
    private EventDao eventDao;


    public static void main(String[] args) {
        SpringApplication.run(QueueReceiver.class, args);
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
            JsonObject jsonObj = JsonParser.parseString(message).getAsJsonObject();
            //LOG.info("Received '{}'", jsonObj.toString() );
            JsonObject header = jsonObj.getAsJsonArray("header").get(0).getAsJsonObject();
            JsonObject body = jsonObj.getAsJsonArray("body").get(0).getAsJsonObject();
            String type = header.get("type").getAsString();
            String operation = header.get("operation").getAsString();

            //LOG.info("Got type: {} operation: {}", type, operation);
            if ("event".equals(type)) {
                handleEvent(operation, body, message);
            } else if ("market".equals(type)) {
                handleMarket(operation, body, message);
            } else if ("outcome".equals(type)) {
                handleOutcome(operation, body, message);
            }
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

    private void handleOutcome(String operation, JsonObject body, String message) {
        if ("create".equals(operation)) {
            String marketId = body.get("marketId").getAsString();
            String outcomeId = body.get("outcomeId").getAsString();
            LOG.info("create outcome: marketid {} outcomeId {} ", marketId, outcomeId);
            EventWrapper event = eventDao.getMarketByEventId(marketId);
            if (event != null) {
                event.addOutcome(marketId, outcomeId, message);
                eventDao.updateEvent(event);
            }
        } else if ("update".equals(operation)) {
            String marketId = body.get("marketId").getAsString();
            String outcomeId = body.get("outcomeId").getAsString();
            LOG.info("update outcome: marketid {} outcomeId {} ", marketId, outcomeId);
            EventWrapper event = eventDao.getMarketByEventId(marketId);
            if (event != null) {
                event.updateOutcome(marketId, outcomeId, message);
                eventDao.updateEvent(event);
            }
        }
    }

    private void handleMarket(String operation, JsonObject body, String message) {
        if ("create".equals(operation)) {
            String eventId = body.get("eventId").getAsString();
            String marketId = body.get("marketId").getAsString();
            LOG.info("create market: eventid {} marketid {}", eventId, marketId);
            EventWrapper event = eventDao.getEventById(eventId);
            if (event != null) {
                event.addMarket(marketId, message);
                eventDao.updateEvent(event);
                eventDao.addEventMarketLink(eventId, marketId);
            }
        } else if ("update".equals(operation)) {
            String eventId = body.get("eventId").getAsString();
            String marketId = body.get("marketId").getAsString();
            LOG.info("update market: eventid {} marketid {}", eventId, marketId);
            EventWrapper event = eventDao.getEventById(eventId);
            if (event != null) {
                event.updateMarket(marketId, message);
                eventDao.updateEvent(event);
            }
        }
    }

    private void handleEvent(String operation, JsonObject body, String message) {
        if ("create".equals(operation)) {

            String eventId = body.get("eventId").getAsString();
            LOG.info("create event id {}", eventId);
            EventWrapper wrapper = new EventWrapper(eventId, message);
            eventDao.saveEvent(wrapper);
        } else if ("update".equals(operation)) {
            String eventId = body.get("eventId").getAsString();
            LOG.info("update event id {}", eventId);
            EventWrapper event = eventDao.getEventById(eventId);
            if (event != null) {
                LOG.info("returned event id: {}", event.getEventId());
                event.addEventUpdate(message);
                eventDao.updateEvent(event);
            }
        }
    }
}
