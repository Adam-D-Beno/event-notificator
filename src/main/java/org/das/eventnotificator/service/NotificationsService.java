package org.das.eventnotificator.service;

import lombok.RequiredArgsConstructor;
import org.das.eventnotificator.dto.NotificationRequest;
import org.das.eventnotificator.model.EventChangeKafkaMessage;
import org.das.eventnotificator.model.EventFieldsChange;
import org.das.eventnotificator.model.Notification;
import org.das.eventnotificator.model.entity.EventFieldsChangeEntity;
import org.das.eventnotificator.model.entity.NotificationEntity;
import org.das.eventnotificator.repository.EventFieldsChangeRepository;
import org.das.eventnotificator.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationsService {

    private static final Logger log = LoggerFactory.getLogger(NotificationsService.class);
    private final NotificationRepository notificationRepository;
    private final EventFieldsChangeRepository  eventFieldsChangeRepository;

    @Transactional
    public void save(EventChangeKafkaMessage kafkaMessage) {
        log.info("Begin to save notificationEntity");
        NotificationEntity entityToSave = mapperToNotificationEntity(kafkaMessage);
        eventFieldsChangeRepository.save(entityToSave.getEventFieldsChangeEntity());
        notificationRepository.save(entityToSave);
        log.info("Saved kafka message in DB");
    }

    public List<Notification> findAllNotReadyUserNotifications() {
        log.info("execute method findAllNotReadyUserNotifications in NotificationsService");
        List<NotificationEntity> notificationsEntity =
                notificationRepository.findAllNotReadyUserNotifications(1L);
        return mapperToNotification(notificationsEntity);
    }

    public boolean markAllUserNotificationRead(NotificationRequest notificationRequest) {
        return notificationRepository.markNotificationAsRead(notificationRequest.notificationIds()) > 0;
    }

    private NotificationEntity mapperToNotificationEntity(EventChangeKafkaMessage kafkaMessage) {
        log.info("Begin mapping kafkaMessage={} to NotificationEntity", kafkaMessage);
        return NotificationEntity.builder()
                .eventId(kafkaMessage.eventId())
                .modifierById(kafkaMessage.modifierById())
                .ownerEventId(kafkaMessage.ownerEventId())
                .eventFieldsChangeEntity(
                        EventFieldsChangeEntity.builder()
                                .name(kafkaMessage.name())
                                .maxPlaces(kafkaMessage.maxPlaces())
                                .date(kafkaMessage.date())
                                .cost(kafkaMessage.cost())
                                .duration(kafkaMessage.duration())
                                .locationId(kafkaMessage.locationId())
                                .status(kafkaMessage.status())
                                .build()
                )
                .registrations(new HashSet<>(kafkaMessage.registrationsOnEvent()))
                .isReady(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<Notification> mapperToNotification(List<NotificationEntity> notificationsEntity) {
        log.info("Begin mapping list notificationEntities to list Notification, list size={}",
                notificationsEntity.size());
        return notificationsEntity
                .stream()
                .map(notificationEntity -> Notification.builder()
                        .notificationId(notificationEntity.getId())
                        .eventId(notificationEntity.getEventId())
                        .modifierById(notificationEntity.getModifierById())
                        .ownerEventId(notificationEntity.getOwnerEventId())
                        .eventFieldsChange(
                                EventFieldsChange.builder()
                                        .name(notificationEntity.getEventFieldsChangeEntity().getName())
                                        .maxPlaces(notificationEntity.getEventFieldsChangeEntity().getMaxPlaces())
                                        .date(notificationEntity.getEventFieldsChangeEntity().getDate())
                                        .cost(notificationEntity.getEventFieldsChangeEntity().getCost())
                                        .duration(notificationEntity.getEventFieldsChangeEntity().getDuration())
                                        .locationId(notificationEntity.getEventFieldsChangeEntity().getLocationId())
                                        .status(notificationEntity.getEventFieldsChangeEntity().getStatus())
                                        .build()
                        )
                        .registrationsOnEvent(notificationEntity.getRegistrations().stream().toList())
                        .isRead(notificationEntity.isReady())
                        .createdAt(notificationEntity.getCreatedAt())
                        .build())
                .toList();
    }
}
