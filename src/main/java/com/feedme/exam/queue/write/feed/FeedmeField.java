package com.feedme.exam.queue.write.feed;

import javax.json.JsonObjectBuilder;

public abstract class FeedmeField {
    private int index;
    private String name;


    public FeedmeField() {
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "index=" + this.index + " name=" + this.name;
    }

    public abstract void addJson(String value, JsonObjectBuilder oBuilder);
}
