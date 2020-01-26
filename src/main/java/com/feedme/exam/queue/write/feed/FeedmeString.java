package com.feedme.exam.queue.write.feed;

import javax.json.JsonObjectBuilder;
import java.util.regex.Pattern;

public class FeedmeString extends FeedmeField {
    private static final Pattern backslashPipe = Pattern.compile("\\\\\\|");

    public FeedmeString() {
    }

    private String handle(String value) {
        return value.replaceAll(backslashPipe.pattern(), "\\|");
    }

    public void addJson(String value, JsonObjectBuilder builder) {
        builder.add(this.getName(), this.handle(value));
    }

}
