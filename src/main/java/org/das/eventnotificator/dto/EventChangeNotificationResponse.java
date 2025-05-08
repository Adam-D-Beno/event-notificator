package org.das.eventnotificator.dto;

import lombok.Builder;
import org.das.eventnotificator.model.EventFieldChange;
import org.das.eventnotificator.model.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
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
