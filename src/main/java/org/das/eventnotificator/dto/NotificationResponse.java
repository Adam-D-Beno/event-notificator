package org.das.eventnotificator.dto;

import lombok.Builder;
import org.das.eventnotificator.model.EventFieldGeneric;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record NotificationResponse(

        Long eventId,
        EventFieldGeneric<String> name,
        EventFieldGeneric<Integer> maxPlaces,
        EventFieldGeneric<LocalDateTime> date,
        EventFieldGeneric<BigDecimal> cost,
        EventFieldGeneric<Integer> duration,
        EventFieldGeneric<Long> locationId
) {
}
