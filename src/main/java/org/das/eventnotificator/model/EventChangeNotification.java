package org.das.eventnotificator.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record EventChangeNotification(

        Long eventId,
        Long userEventChangedId,
        Long ownerEventId,
        EventFieldChange<String> name,
        EventFieldChange<Integer> maxPlaces,
        EventFieldChange<LocalDateTime> date,
        EventFieldChange<BigDecimal> cost,
        EventFieldChange<Integer> duration,
        EventFieldChange<Long> locationId,
        EventFieldChange<EventStatus> status,
        List<Long> userRegistrationsOnEvent
) {
}
