package com.feedme.exam.queue.write.feed;

import org.jetbrains.annotations.NotNull;

import javax.json.JsonObjectBuilder;
import java.math.BigInteger;

public class FeedmeBigInteger extends FeedmeField {

    public FeedmeBigInteger() {
        super();
    }

    @NotNull
    private BigInteger handle(String integer) {
        return new BigInteger(integer);
    }

    public void addJson(String value, @NotNull JsonObjectBuilder builder) {
        builder.add(this.getName(), this.handle(value));
    }

}
