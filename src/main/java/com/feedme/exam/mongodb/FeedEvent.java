package com.feedme.exam.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class FeedEvent {
    public String getText() {
        return text;
    }

    private final String text;

    public String getId() {
        return id;
    }

    @Id
    private final String id;

    public FeedEvent(String id, String text) {
        this.id = id;
        this.text = text;
    }
}
