package com.feedme.exam.feed;

import javax.json.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class FeedmeType {
    private String typeName;
    private Map<String, List<FeedmeField>> sections = new TreeMap();

    public FeedmeType(String name) {
        this.typeName = name;
    }

    public void addFieldToSection(String sectionName, FeedmeField field) {
        List<FeedmeField> section = sections.get(sectionName);
        if (section == null) {
            section = new ArrayList<>();
        }
        section.add(field);
        sections.put(sectionName, section);
    }

    private JsonArray addSection(String name, String[] fields, JsonBuilderFactory factory)
    {
        JsonObjectBuilder oBuilder = factory.createObjectBuilder();
        sections.get(name).stream().forEach(f -> f.addJson(fields, oBuilder));

        JsonObject x = oBuilder.build();
        return factory.createArrayBuilder().add(x).build();
    }

    public JsonObject buildJson(String[] fields)
    {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder builder = factory.createObjectBuilder();
        sections.keySet().stream().forEach(key -> builder.add(key, factory.createArrayBuilder(addSection(key, fields, factory))));

        return builder.build();
    }


}
