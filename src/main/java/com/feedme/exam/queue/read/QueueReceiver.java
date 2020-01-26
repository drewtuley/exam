package com.feedme.exam.queue.read;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.addToSet;

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
    private MongoCollection<Document> collection;


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
            JsonObject header = jsonObj.getAsJsonObject("header");
            JsonObject body = jsonObj.getAsJsonObject("body");
            String type = header.get("type").getAsString();
            String operation = header.get("operation").getAsString();

            //LOG.info("Got type: {} operation: {}", type, operation);
            if ("event".equals(type)) {
                handleEvent(operation, body.get("eventId").getAsString(), message);
            } else if ("market".equals(type)) {
                handleMarket(operation, body, message);
            } else if ("outcome".equals(type)) {
                handleOutcome(body, message);
            }
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

    private void handleOutcome(JsonObject body, String json) {
        String marketId = body.get("marketId").getAsString();
        LOG.info("Handle outcome to market: {}", marketId);
        Document outcomeBase = Document.parse(json);
        collection.updateOne(eq("markets._id", marketId), addToSet("markets.$[item].outcomes", outcomeBase), new UpdateOptions()
                .arrayFilters(
                        Collections.singletonList(Filters.in("item._id", Collections.singletonList(marketId)))
                ));
    }

    private void handleMarket(String operation, JsonObject body, String json) {
        String mktEventId = body.get("eventId").getAsString();
        String marketId = body.get("marketId").getAsString();
        LOG.info("Handle {} to market: {} on event {}", operation, marketId, mktEventId);
        if ("create".equals(operation)) {
            Document marketBase = Document.parse("{\"market\": [" + json + "]}").append("_id", marketId);
            collection.updateOne(eq("_id", mktEventId), addToSet("markets", marketBase));
        } else if ("update".equals(operation)) {
            Document mktUpdate = Document.parse(json);
            collection.updateOne(eq("markets._id", marketId), addToSet("markets.$[item].market", mktUpdate), new UpdateOptions()
                    .arrayFilters(
                            Collections.singletonList(Filters.in("item._id", Collections.singletonList(marketId)))
                    ));
        }
    }

    private void handleEvent(String operation, String eventId, String json) {
        LOG.info("Handle {} to event: {}", operation, eventId);
        if ("create".equals(operation)) {
            Document fixture = Document.parse("{\"events\":[" + json + "], \"markets\":[]}").append("_id", eventId);
            collection.insertOne(fixture);
        } else if ("update".equals(operation)) {
            Document event2 = Document.parse(json);
            collection.updateOne(eq("_id", eventId), addToSet("events", event2));
        }
    }
}
