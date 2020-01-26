package com.feedme.exam.queue.read;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${feedme.database}")
    private String database;

    @Value("${feedme.collection}")
    private String collection;

    @NotNull
    @Override
    public MongoClient mongoClient() {
        return MongoClients.create();
    }

    @NotNull
    @Override
    protected String getDatabaseName() {
        return this.database;
    }

    @NotNull
    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }

    @Bean
    public MongoCollection<Document> mongoCollection() {
        return this.mongoTemplate().getCollection(this.collection);
    }
}