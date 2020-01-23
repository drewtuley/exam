package com.feedme.exam;

import com.feedme.exam.feed.FeedmeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExamApplication implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(ExamApplication.class);

    @Value("${feedme.host}")
    private String host;

    @Value("${feedme.feed_port}")
    private Integer feed_port;

    @Value("${feedme.types_port}")
    private Integer types_port;


    public static void main(String[] args) {
        SpringApplication.run(ExamApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Running on host {} and port {}", this.host, this.feed_port);
        FeedmeClient client = new FeedmeClient();

        client.startConnection(this.host, this.feed_port, this.types_port);
        String message;
        while ((message = client.getNextJson()) != null) {
            LOG.info(message);
        }
    }
}
