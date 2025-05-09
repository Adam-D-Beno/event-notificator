package org.das.eventnotificator.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record Notification(
        Long notificationId,
        Long eventId,
        Long modifierById,
        Long ownerEventId,
        EventFieldsChange eventFieldsChange,
        List<Long> registrationsOnEvent,
        boolean isRead,
        LocalDateTime createdAt
) {}
