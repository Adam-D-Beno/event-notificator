package org.das.eventnotificator.model;

import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record EventNotification(
        Long notificationId,
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
        List<Long> userRegistrationsOnEvent,
        boolean isRead,
        LocalDateTime notificationDate
) {}
