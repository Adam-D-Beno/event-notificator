package org.das.eventnotificator.service;

import lombok.RequiredArgsConstructor;
import org.das.eventnotificator.dto.NotificationRequest;
import org.das.eventnotificator.model.EventChangeKafkaMessage;
import org.das.eventnotificator.model.EventFieldsChange;
import org.das.eventnotificator.model.Notification;
import org.das.eventnotificator.model.entity.EventFieldsChangeEntity;
import org.das.eventnotificator.model.entity.NotificationEntity;
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
        log.info("Begin to save notificationEntity");
        notificationRepository.save(mapperToNotificationEntity(kafkaMessage));
        log.info("Saved kafka message in DB");
    }

    public List<Notification> findAllNotReadyUserNotifications() {
        log.info("execute method findAllNotReadyUserNotifications in NotificationsService");
        List<NotificationEntity> notificationsEntity =
                notificationRepository.findAllNotReadyUserNotifications(1L);
        return mapperToNotifications(notificationsEntity);
    }

    public boolean markAllUserNotificationRead(NotificationRequest notificationRequest) {
        return notificationRepository.markNotificationAsRead(notificationRequest.notificationIds()) > 0;
    }

    private NotificationEntity mapperToNotificationEntity(EventChangeKafkaMessage kafkaMessage) {
        return NotificationEntity.builder()
                .eventId(kafkaMessage.eventId())
                .modifierById(kafkaMessage.modifierById())
                .ownerEventId(kafkaMessage.ownerEventId())
                .eventFieldsChangeEntity(
                        EventFieldsChangeEntity.builder()
                                .name(kafkaMessage.name())
                                .maxPlaces(kafkaMessage.MaxPlaces())
                                .date(kafkaMessage.date())
                                .cost(kafkaMessage.cost())
                                .duration(kafkaMessage.duration())
                                .locationId(kafkaMessage.locationId())
                                .status(kafkaMessage.status())
                                .build()
                )
                .registrationsOnEvent(kafkaMessage.registrationsOnEvent())
                .isReady(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<Notification> mapperToNotifications(List<NotificationEntity> notificationsEntity) {
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
                        .registrationsOnEvent(notificationEntity.getRegistrationsOnEvent())
                        .isRead(notificationEntity.isReady())
                        .createdAt(notificationEntity.getCreatedAt())
                        .build())
                .toList();
    }
}
