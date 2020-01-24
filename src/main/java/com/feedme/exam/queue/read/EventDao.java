package com.feedme.exam.queue.read;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.query.Criteria.where;


@Service
public class EventDao {

    @Autowired
    private MongoTemplate template;

    public void saveEvent(EventWrapper wrapper) {

        template.insert(wrapper);
    }

    public void updateEvent(EventWrapper wrapper) {
        template.save(wrapper);
    }


    public EventWrapper getEventById(String eventId) {
        EventWrapper event = template.findOne(new Query(where("eventId").is(eventId)), EventWrapper.class);

        return event;
    }

    public EventWrapper getMarketByEventId(String marketId) {
        EventMarketLink link = template.findOne(new Query(where("marketId").is(marketId)), EventMarketLink.class);
        if (link != null) {
            return this.getEventById(link.getEventId());
        }
        return null;
    }

    public void addEventMarketLink(String eventId, String marketId) {
        template.save(new EventMarketLink(eventId, marketId));
    }
}
