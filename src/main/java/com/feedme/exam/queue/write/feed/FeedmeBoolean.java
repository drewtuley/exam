package com.feedme.exam.queue.write.feed;

import javax.json.JsonObjectBuilder;

public class FeedmeBoolean extends FeedmeField {

    public FeedmeBoolean() {
    }

    private Boolean handle(String s) {
        if ("1".equals(s)) {
            return Boolean.TRUE;
        } else if ("0".equals(s)) {
            return Boolean.FALSE;
        } else {
            return Boolean.parseBoolean(s);
        }
    }

    @Override
    public void addJson(String value, JsonObjectBuilder builder) {
        builder.add(this.getName(), this.handle(value));
    }
}
