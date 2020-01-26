package com.feedme.exam.queue.write.feed;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FeedmeBigIntegerTest {
    private JsonObjectBuilder builder;

    @BeforeEach
    void before()
    {
        this.builder = Json.createBuilderFactory(null).createObjectBuilder();
    }

    @Test
    void testJson()
    {
        // given
        FeedmeBigInteger underTest = new FeedmeBigInteger();
        underTest.setName("test");

        // when
        underTest.addJson("123", this.builder);

        // then
        JsonObject json = this.builder.build();
        assertEquals("{\"test\":123}", json.toString());
    }
}