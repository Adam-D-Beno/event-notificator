package org.das.eventnotificator.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public record NotificationRequest(

        @Size(min = 1)
        List<Long> notificationIds
) {}
