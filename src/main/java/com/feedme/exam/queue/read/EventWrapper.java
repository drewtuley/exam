package com.feedme.exam.queue.read;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document
public class EventWrapper {
    @Id
    private String eventId;
    private String eventJson;
    private List<String> eventUpdates;
    private Map<String, List<String>> markets;
    private Map<String, Map<String, List<String>>> outcomes;

    public EventWrapper(String eventId, String eventJson) {
        this.eventId = eventId;
        this.eventJson = eventJson;
        this.eventUpdates = new ArrayList<>();
        this.markets = new HashMap<String, List<String>>();
        this.outcomes = new HashMap<String, Map<String, List<String>>>();
    }

    public String getEventId() {
        return eventId;
    }

    public void addEventUpdate(String message) {
        this.eventUpdates.add(message);
    }

    public void addMarket(String marketId, String message) {
        List<String> market = new ArrayList<>();
        market.add(message);
        this.markets.put(marketId, market);

        this.outcomes.put(marketId, new HashMap<String, List<String>>());
    }

    public void updateMarket(String marketId, String message) {
        List<String> market = this.markets.get(marketId);
        if (market != null) {
            market.add(message);
            this.markets.put(marketId, market);
        } else {
            addMarket(marketId, message);
        }
    }

    public void addOutcome(String marketId, String outcomeId, String message) {
        Map<String, List<String>> outcome = this.outcomes.get(marketId);
        ArrayList<String> outcome_list = new ArrayList<>();
        outcome_list.add(message);
        outcome.put(outcomeId, outcome_list);
        this.outcomes.put(marketId, outcome);
    }

    public void updateOutcome(String marketId, String outcomeId, String message) {
        Map<String, List<String>> outcome = this.outcomes.get(marketId);
        if (outcome != null) {
            List<String> outcome_list = outcome.get(outcomeId);
            if (outcome_list != null) {
                outcome_list.add(message);
                outcome.put(outcomeId, outcome_list);
                this.outcomes.put(marketId, outcome);
            } else {
                addOutcome(marketId, outcomeId, message);
            }
        }
    }
}
