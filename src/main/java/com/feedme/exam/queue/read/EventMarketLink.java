package com.feedme.exam.queue.read;

import org.springframework.data.annotation.Id;

public class EventMarketLink {
    private String eventId;

    @Id
    private String marketId;


    public EventMarketLink(String eventId, String marketId) {
        this.eventId = eventId;
        this.marketId = marketId;
    }

    public String getEventId() {
        return this.eventId;
    }
}
