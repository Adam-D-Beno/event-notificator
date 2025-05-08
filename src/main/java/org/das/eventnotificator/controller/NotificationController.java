package org.das.eventnotificator.controller;

import lombok.RequiredArgsConstructor;
import org.das.eventnotificator.dto.EventChangeNotificationResponse;
import org.das.eventnotificator.dto.NotificationRequest;
import org.das.eventnotificator.service.KafkaEventConsumerService;
import org.das.eventnotificator.service.NotificationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationsService notificationsService;

    @GetMapping
    public ResponseEntity<List<EventChangeNotificationResponse>> getAllNotReadyNotifications() {
        log.info("Get request All Not Ready Notifications");
        return ResponseEntity.ok(List.of());
    }

    @PostMapping
    public ResponseEntity<Void> SetAllNotificationsAsRead(
            @RequestBody NotificationRequest notificationRequest
    ) {
        log.info("POST request Set All Notifications As Read");
        return ResponseEntity.noContent().build();
    }
}
