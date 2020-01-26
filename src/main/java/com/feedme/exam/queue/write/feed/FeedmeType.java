package com.feedme.exam.queue.write.feed;

import com.google.common.collect.ImmutableList;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.*;


public class FeedmeType {
    private final Map<String, List<FeedmeField>> sections = new HashMap<>();

    public void addFieldToSection(String sectionName, FeedmeField field) {
        List<FeedmeField> section = sections.get(sectionName);
        if (section == null) {
            section = new ArrayList<>();
        }
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

    public static void main(String[] args) {
        test();
    }

    private static void test() {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder builder = factory.createObjectBuilder();

        JsonObject body = builder.add("eventId", "1234-4321").add("type", "event").build();
        JsonObject main = builder.add("body", body).build();
        System.out.println(main.toString());

        ImmutableList<String> numbers = ImmutableList.of("10", "20", "30", "40");
        ImmutableList<String> sections = ImmutableList.of("one", "two");

        JsonObjectBuilder obuilder = factory.createObjectBuilder();

        sections.forEach(sect -> {
            numbers.forEach(n -> builder.add(n, Integer.parseInt(n)));
            JsonObject x = builder.build();
            obuilder.add(sect, x);
        });

        main = obuilder.build();
        System.out.println(main.toString());
    }

}
