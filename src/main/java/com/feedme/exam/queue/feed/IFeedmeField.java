package com.feedme.exam.queue.feed;

import javax.json.JsonObjectBuilder;

public interface IFeedmeField {
    public int getIndex();
    public void setIndex(int index);

    public String getName();
    public void setName(String name);

    public void addJson(String[] fields, JsonObjectBuilder builder);
}
