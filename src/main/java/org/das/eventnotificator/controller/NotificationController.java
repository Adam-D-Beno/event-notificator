package org.das.eventnotificator.controller;

import lombok.RequiredArgsConstructor;
import org.das.eventnotificator.dto.NotificationResponse;
import org.das.eventnotificator.dto.NotificationRequest;
import org.das.eventnotificator.model.Notification;
import org.das.eventnotificator.security.CustomUserDetail;
import org.das.eventnotificator.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> findAllNotReadyNotifications(
            @AuthenticationPrincipal CustomUserDetail authUser
            ) {
        log.info("Get request All Not Ready Notifications");
        List<Notification> notReadyUserNotifications =
                notificationService.findAllNotReadyUserNotifications(authUser);
        List<NotificationResponse> response = notReadyUserNotifications
                .stream()
                .map(notification ->  NotificationResponse.builder()
                        .eventId(notification.eventId())
                        .name(notification.eventFieldsChange().name())
                        .maxPlaces(notification.eventFieldsChange().maxPlaces())
                        .date(notification.eventFieldsChange().date())
                        .cost(notification.eventFieldsChange().cost())
                        .duration(notification.eventFieldsChange().duration())
                        .locationId(notification.eventFieldsChange().locationId())
                        .build()
                ).toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> markAllNotificationsAsRead(
            @RequestBody NotificationRequest notificationRequest,
            @AuthenticationPrincipal CustomUserDetail authUser
    ) {
        log.info("POST request Set All Notifications As Read");
        notificationService.markAllUserNotificationRead(notificationRequest, authUser);
        return ResponseEntity.noContent().build();
    }
}
