package com.feedme.exam.queue.feed;

import javax.json.JsonObjectBuilder;
import java.math.BigInteger;

public class FeedmeBigInteger extends FeedmeField {
    public FeedmeBigInteger(int index, String name) {
        super(index, name);
    }

    public FeedmeBigInteger() {
        super();
    }
    private BigInteger handle(String integer)
    {
        BigInteger i = new BigInteger(integer);
        return i;
    }

    @Override
    public void addJson(String[] fields, JsonObjectBuilder builder) {
        builder.add(this.getName(), this.handle(fields[this.getIndex()+1]));
        return ;
    }
}
