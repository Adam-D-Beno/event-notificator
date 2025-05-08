package org.das.eventnotificator.controller;

import lombok.RequiredArgsConstructor;
import org.das.eventnotificator.dto.EventChangeNotificationResponse;
import org.das.eventnotificator.dto.NotificationRequest;
import org.das.eventnotificator.model.EventNotification;
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
    public ResponseEntity<List<EventChangeNotificationResponse>> findAllNotReadyNotifications() {
        log.info("Get request All Not Ready Notifications");
        List<EventNotification> notReadyUserNotifications = notificationsService.findAllNotReadyUserNotifications();
        return ResponseEntity.ok(
                notReadyUserNotifications
                        .stream()
                        .map(notification ->  EventChangeNotificationResponse.builder()
                                .eventId(notification.eventId())
                                .name(notification.name())
                                .maxPlaces(notification.maxPlaces())
                                .date(notification.date())
                                .cost(notification.cost())
                                .duration(notification.duration())
                                .locationId(notification.locationId())
                                .build()
                        ).toList()
        );
    }

    @PostMapping
    public ResponseEntity<Void> markAllNotificationsAsRead(
            @RequestBody NotificationRequest notificationRequest
    ) {
        log.info("POST request Set All Notifications As Read");
        boolean res = notificationsService.markAllUserNotificationRead(notificationRequest);
        if (res) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
