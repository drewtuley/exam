package com.feedme.exam.feed;

import javax.json.JsonObjectBuilder;

public class FeedmeBoolean extends FeedmeField {
    public FeedmeBoolean(int index, String name) {
        super(index, name);
    }

    public FeedmeBoolean(){}

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
    public void addJson(String[] fields, JsonObjectBuilder builder) {
        builder.add(this.getName(), this.handle(fields[this.getIndex()+1]));
        return ;
    }
}
