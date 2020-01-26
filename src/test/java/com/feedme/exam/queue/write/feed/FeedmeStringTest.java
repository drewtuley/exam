package com.feedme.exam.queue.write.feed;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import static org.junit.jupiter.api.Assertions.*;

class FeedmeStringTest {
    private JsonObjectBuilder builder;

    @BeforeEach
    void before()
    {
        this.builder = Json.createBuilderFactory(null).createObjectBuilder();
    }

    @Test
    void testStraightString()
    {
        // given
        FeedmeString underTest = new FeedmeString();
        underTest.setName("test");

        // when
        underTest.addJson("test", this.builder);

        // then
        JsonObject json = this.builder.build();
        assertEquals("{\"test\":\"test\"}", json.toString());

    }

    @Test
    void testStringWithEscapedPipe()
    {
        // given
        FeedmeString underTest = new FeedmeString();
        underTest.setName("test");

        // when
        underTest.addJson("\\|test\\|", this.builder);

        // then
        JsonObject json = this.builder.build();
        assertEquals("{\"test\":\"|test|\"}", json.toString());

    }

}