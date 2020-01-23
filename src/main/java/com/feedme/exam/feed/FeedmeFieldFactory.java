package com.feedme.exam.feed;

import com.google.common.collect.ImmutableMap;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.Map;
import java.util.function.Function;


public class FeedmeFieldFactory {
    private static FeedmeFieldFactory ourInstance = new FeedmeFieldFactory();

    private final Map<String, Function<String, FeedmeField>> MAPPED_FIELDS;

    public static FeedmeFieldFactory getInstance() {
        return ourInstance;
    }

    private FeedmeFieldFactory() {
        MAPPED_FIELDS = ImmutableMap.<String, Function<String, FeedmeField>>builder()
                .put("string", x -> new FeedmeString())
                .put("integer", x -> new FeedmeBigInteger())
                .put("boolean", x -> new FeedmeBoolean())
                .build();
    }


    public FeedmeField getFieldBySpecification(NamedNodeMap spec) {
        Node dataType = spec.getNamedItem("datatype");
        Node index = spec.getNamedItem("index");
        Node name = spec.getNamedItem("name");
        int idx = Integer.parseInt(index.getNodeValue());

        FeedmeField feedField =  MAPPED_FIELDS.get(dataType.getNodeValue()).apply("");
        feedField.setIndex(idx);
        feedField.setName(name.getNodeValue());

        return feedField;
    }
}
