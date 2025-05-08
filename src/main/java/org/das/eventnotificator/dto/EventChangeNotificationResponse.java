package org.das.eventnotificator.dto;

import org.das.eventnotificator.model.EventFieldChange;
import org.das.eventnotificator.model.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventChangeNotificationResponse(

        Long eventId,
        EventFieldChange<String> name,
        EventFieldChange<Integer> maxPlaces,
        EventFieldChange<LocalDateTime> date,
        EventFieldChange<BigDecimal> cost,
        EventFieldChange<Integer> duration,
        EventFieldChange<Long> locationId
) {
}
