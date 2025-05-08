package org.das.eventnotificator.service;

import lombok.RequiredArgsConstructor;
import org.das.eventnotificator.dto.NotificationRequest;
import org.das.eventnotificator.model.EventChangeKafkaMessage;
import org.das.eventnotificator.model.EventFieldChange;
import org.das.eventnotificator.model.EventNotification;
import org.das.eventnotificator.model.entity.EventNotificationEntity;
import org.das.eventnotificator.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationsService {

    private static final Logger log = LoggerFactory.getLogger(NotificationsService.class);
    private final NotificationRepository notificationRepository;

    public void save(EventChangeKafkaMessage kafkaMessage) {
        log.info("Begin to save kafka message in DB");
        EventNotificationEntity eventNotificationEntityToSave = new EventNotificationEntity(
                null,
                kafkaMessage.eventId(),
                kafkaMessage.userEventChangedId(),
                kafkaMessage.ownerEventId(),
                new EventFieldChange<>(kafkaMessage.name().getOldValue(), kafkaMessage.name().getNewValue()),
                new EventFieldChange<>(kafkaMessage.MaxPlaces().getOldValue(), kafkaMessage.MaxPlaces().getNewValue()),
                new EventFieldChange<>(kafkaMessage.date().getOldValue(), kafkaMessage.date().getNewValue()),
                new EventFieldChange<>(kafkaMessage.cost().getOldValue(), kafkaMessage.cost().getNewValue()),
                new EventFieldChange<>(kafkaMessage.duration().getOldValue(), kafkaMessage.duration().getNewValue()),
                new EventFieldChange<>(kafkaMessage.locationId().getOldValue(), kafkaMessage.locationId().getNewValue()),
                new EventFieldChange<>(kafkaMessage.status().getOldValue(), kafkaMessage.status().getNewValue()),
                kafkaMessage.userRegistrationsOnEvent(),
                false,
                LocalDateTime.now());

        notificationRepository.save(eventNotificationEntityToSave);
        log.info("Saved kafka message in DB");
    }

    public List<EventNotification> findAllNotReadyUserNotifications() {
        log.info("execute method findAllNotReadyUserNotifications in NotificationsService");
        List<EventNotificationEntity> notificationsEntity =
                notificationRepository.findAllNotReadyUserNotifications(1L);
        return notificationsEntity
                .stream()
                .map(eventNotificationEntity -> EventNotification.builder()
                 .notificationId(eventNotificationEntity.getId())
                 .eventId(eventNotificationEntity.getEventId())
                 .userEventChangedId(eventNotificationEntity.getUserEventChangedId())
                 .ownerEventId(eventNotificationEntity.getOwnerEventId())
                 .name(eventNotificationEntity.getName())
                 .maxPlaces(eventNotificationEntity.getMaxPlaces())
                 .date(eventNotificationEntity.getDate())
                 .cost(eventNotificationEntity.getCost())
                 .duration(eventNotificationEntity.getDuration())
                 .locationId(eventNotificationEntity.getLocationId())
                 .status(eventNotificationEntity.getStatus())
                 .userRegistrationsOnEvent(eventNotificationEntity.getUserRegistrationsOnEvent())
                 .isRead(eventNotificationEntity.isRead())
                 .notificationDate(eventNotificationEntity.getNotificationDate())
                 .build()).toList();
    }

    public boolean markAllUserNotificationRead(NotificationRequest notificationRequest) {
        return notificationRepository.markNotificationAsRead(notificationRequest.notificationIds()) > 0;
    }
}
