package com.feedme.exam.queue.write.feed;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FeedmeType {
    private final Map<String, List<FeedmeField>> sections = new HashMap<>();

    public void addFieldToSection(String sectionName, FeedmeField field) {
        List<FeedmeField> section =sections.getOrDefault(sectionName, new ArrayList<>());
        section.add(field);
        sections.put(sectionName, section);
    }

    private JsonObject addSection(String name, List<String> fields, JsonObjectBuilder oBuilder) {
        sections.get(name).forEach(f -> {
            String value = fields.get(f.getIndex());
            f.addJson(value, oBuilder);
        });

        return oBuilder.build();
    }

    public JsonObject buildJson(List<String> fields) {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder builder = factory.createObjectBuilder();
        JsonObjectBuilder innerBuilder = factory.createObjectBuilder();

        sections.keySet().forEach(key -> builder.add(key, addSection(key, fields, innerBuilder)));

        return builder.build();
    }

}
