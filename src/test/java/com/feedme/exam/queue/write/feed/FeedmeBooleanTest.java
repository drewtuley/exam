package com.feedme.exam.queue.write.feed;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FeedmeBooleanTest {
    private JsonObjectBuilder builder;

    @BeforeEach
    void before()
    {
        this.builder = Json.createBuilderFactory(null).createObjectBuilder();
    }


    @ParameterizedTest
    @CsvSource(value = {"0:false", "1:true"}, delimiter = ':')
    void testValidValues(String input, String expected)
    {
        // given
        FeedmeBoolean underTest = new FeedmeBoolean();
        underTest.setName("test");

        // when
        underTest.addJson(input, this.builder);

        // then
        JsonObject json = this.builder.build();
        assertEquals("{\"test\":"+expected+"}", json.toString());
    }


}