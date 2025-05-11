package org.das.eventnotificator.controller;

import lombok.RequiredArgsConstructor;
import org.das.eventnotificator.dto.NotificationResponse;
import org.das.eventnotificator.dto.NotificationRequest;
import org.das.eventnotificator.model.EventChangeKafkaMessage;
import org.das.eventnotificator.model.EventFieldGeneric;
import org.das.eventnotificator.model.EventStatus;
import org.das.eventnotificator.model.Notification;
import org.das.eventnotificator.security.jwt.CustomUserDetail;
import org.das.eventnotificator.service.NotificationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationsService notificationsService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> findAllNotReadyNotifications(
            @AuthenticationPrincipal CustomUserDetail authUser
            ) {
        log.info("Get request All Not Ready Notifications");
        List<Notification> notReadyUserNotifications =
                notificationsService.findAllNotReadyUserNotifications(authUser);
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
        boolean res = notificationsService.markAllUserNotificationRead(
                notificationRequest,
                authUser);
        if (!res) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ) {
//        EventChangeKafkaMessage changeKafkaMessage = EventChangeKafkaMessage.builder()
//                .eventId(1L)
//                .modifierById(1L)
//                .ownerEventId(1L)
//                .name(new EventFieldGeneric<>("oldName", "newName"))
//                .maxPlaces(new EventFieldGeneric<>(10, 20))
//                .date(new EventFieldGeneric<>(LocalDateTime.now(), LocalDateTime.now()))
//                .cost(new EventFieldGeneric<>(BigDecimal.ONE, BigDecimal.TEN))
//                .duration(new EventFieldGeneric<>(10, 60))
//                .locationId(new EventFieldGeneric<>(8L, 10L))
//                .status(new EventFieldGeneric<>(EventStatus.WAIT_START, EventStatus.STARTED))
//                .registrationsOnEvent(List.of(1L, 2L, 3L))
//                .build();
//        notificationsService.save(changeKafkaMessage);
        log.info("ID пользователя={}, login={}, role={}",
                customUserDetail.getId(),customUserDetail.getUsername(),
                customUserDetail.getAuthorities().toString());
        return ResponseEntity.ok("Its work");
    }

}
