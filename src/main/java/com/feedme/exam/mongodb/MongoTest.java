package com.feedme.exam.mongodb;

import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@SpringBootApplication
public class MongoTest implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(MongoTest.class);

    public static void main(String[] args) {
        SpringApplication.run(MongoTest.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        MongoOperations mongoOps = new MongoTemplate(MongoClients.create(), "database");

        mongoOps.dropCollection("feedEvent");
        mongoOps.dropCollection("person");


        mongoOps.insert(new Person("Joe", 34));
        Person x = mongoOps.findOne(new Query(where("name").is("Joe")), Person.class);
        LOG.info(x.toString());

        mongoOps.insert(new FeedEvent("12", "blah blah blah"));
        FeedEvent fe = mongoOps.findOne(new Query(where("id").is("12")), FeedEvent.class);
        LOG.info("Found FE id:{}", fe.getId());


        //mongoOps.dropCollection("person");
        //mongoOps.dropCollection("feedEvent");

    }
}
