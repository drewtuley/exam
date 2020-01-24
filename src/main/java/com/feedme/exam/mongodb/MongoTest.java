package com.feedme.exam.mongodb;

import com.feedme.exam.queue.read.EventMarketLink;
import com.feedme.exam.queue.read.EventWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@SpringBootApplication
public class MongoTest implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(MongoTest.class);


    @Autowired
    private MongoTestConfig config;


    public static void main(String[] args) {
        SpringApplication.run(MongoTest.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        MongoOperations mongoOps = config.mongoTemplate();
        //MongoOperations mongoOps = new MongoTemplate(MongoClients.create(), "database");

        mongoOps.dropCollection("feedEvent");
        mongoOps.dropCollection("person");


        mongoOps.insert(new Person("Joe", 34));
        Person x = mongoOps.findOne(new Query(where("name").is("Joe")), Person.class);
        LOG.info(x.toString());

        mongoOps.insert(new FeedEvent("12", "blah blah blah"));
        FeedEvent fe = mongoOps.findOne(new Query(where("id").is("12")), FeedEvent.class);
        LOG.info("Found FE id:{}", fe.getId());


        EventWrapper obj = mongoOps.findOne(new Query(where("eventId").is("96dc3f5c-7b66-4ce8-aaa2-200694e9de6c")), EventWrapper.class);
        LOG.info("EventWrapper = {}", obj);

        List<EventMarketLink> links = mongoOps.findAll(EventMarketLink.class);
        LOG.info("# Links {}", links.size());

        //mongoOps.dropCollection("person");
        //mongoOps.dropCollection("feedEvent");
        mongoOps.dropCollection("eventWrapper");
        mongoOps.dropCollection("eventMarketLink");
    }
}
