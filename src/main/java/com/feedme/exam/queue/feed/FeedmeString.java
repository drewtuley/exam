package com.feedme.exam.queue.feed;

import javax.json.JsonObjectBuilder;
import java.util.regex.Pattern;

public class FeedmeString extends FeedmeField {
    private static final Pattern backslashPipe = Pattern.compile("\\\\\\|");

    public FeedmeString(int index, String name) {
        super(index, name);
    }
    public FeedmeString(){}

    private String handle(String value) {
        return value.replaceAll(backslashPipe.pattern(), "\\|");
    }

    @Override
    public void addJson(String[] fields, JsonObjectBuilder builder) {
        builder.add(this.getName(), this.handle(fields[this.getIndex()+1]));
        return ;
    }
}
