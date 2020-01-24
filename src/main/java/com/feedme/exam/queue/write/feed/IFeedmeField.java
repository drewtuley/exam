package com.feedme.exam.queue.write.feed;

import javax.json.JsonObjectBuilder;
import java.util.List;

public interface IFeedmeField {
    public int getIndex();
    public void setIndex(int index);

    public String getName();
    public void setName(String name);

    public void addJson(List<String> fields, JsonObjectBuilder builder);
}
