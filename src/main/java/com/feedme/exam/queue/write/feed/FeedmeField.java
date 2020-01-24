package com.feedme.exam.queue.write.feed;

import javax.json.JsonObjectBuilder;
import java.util.List;

public abstract class FeedmeField implements IFeedmeField {
    private int index;
    private String name;



    public FeedmeField(int index, String name) {
        this.index = index;
        this.name = name;

    }

    public FeedmeField()
    {    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return "index="+this.index+" name="+this.name;
    }


    public abstract void addJson(List<String> fields, JsonObjectBuilder builder);
}
