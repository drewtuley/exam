package com.feedme.exam.queue.write.feed;

import javax.json.JsonObjectBuilder;
import java.util.List;

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
    public void addJson(List<String> fields, JsonObjectBuilder builder) {
        builder.add(this.getName(), this.handle(fields.get(this.getIndex())));
        return ;
    }
}
