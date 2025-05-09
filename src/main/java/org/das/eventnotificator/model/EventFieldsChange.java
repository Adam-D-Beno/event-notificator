package org.das.eventnotificator.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record EventFieldsChange(
        EventFieldGeneric<String> name,
        EventFieldGeneric<Integer> maxPlaces,
        EventFieldGeneric<LocalDateTime> date,
        EventFieldGeneric<BigDecimal> cost,
        EventFieldGeneric<Integer> duration,
        EventFieldGeneric<Long> locationId,
        EventFieldGeneric<EventStatus> status
) {
}
