package org.das.eventnotificator.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.das.eventnotificator.dto.NotificationRequest;
import org.das.eventnotificator.model.EventChangeKafkaMessage;
import org.das.eventnotificator.model.EventFieldsChange;
import org.das.eventnotificator.model.Notification;
import org.das.eventnotificator.model.entity.EventFieldsChangeEntity;
import org.das.eventnotificator.model.entity.NotificationEntity;
import org.das.eventnotificator.repository.EventFieldsChangeRepository;
import org.das.eventnotificator.repository.NotificationRepository;
import org.das.eventnotificator.security.CustomUserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;
    private final EventFieldsChangeRepository  eventFieldsChangeRepository;

    @Transactional
    public void save(EventChangeKafkaMessage kafkaMessage) {
        log.info("Begin to save notificationEntity");
        NotificationEntity entityToSave = mapperToNotificationEntity(kafkaMessage);
        entityToSave.setToFieldsChange();
        eventFieldsChangeRepository.save(entityToSave.getFieldsChange());
        notificationRepository.save(entityToSave);
        log.info("Saved kafka message in DB");
    }

    public List<Notification> findAllNotReadyUserNotifications(CustomUserDetail authUser) {
        log.info("execute method findAllNotReadyUserNotifications in NotificationsService");
        List<NotificationEntity> notificationsEntity =
                notificationRepository.findAllNotReadyUserNotifications(authUser.getId());
        return mapperToNotification(notificationsEntity);
    }

    public void markAllUserNotificationRead(
            NotificationRequest notificationRequest,
            CustomUserDetail authUser
    ) {
        int res = notificationRepository.markNotificationAsRead(
                notificationRequest.notificationIds(),
                authUser.getId()
        );
        if (res == 0) {
            log.error("Entity Not Found Exception");
            throw new EntityNotFoundException("No such Element found");
        }
    }

    public List<Long> deleteNotificationByMoreDays(int days) {
        log.info("Begin delete notification More Days={}", days);
        return notificationRepository.deleteNotificationByDate(days);
    }

    private NotificationEntity mapperToNotificationEntity(EventChangeKafkaMessage kafkaMessage) {
        log.info("Begin mapping entity to kafkaMessage={} to NotificationEntity", kafkaMessage);
        return NotificationEntity.builder()
                .eventId(kafkaMessage.eventId())
                .modifierById(kafkaMessage.modifierById())
                .ownerEventId(kafkaMessage.ownerEventId())
                .fieldsChange(
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
                                        .name(notificationEntity.getFieldsChange().getName())
                                        .maxPlaces(notificationEntity.getFieldsChange().getMaxPlaces())
                                        .date(notificationEntity.getFieldsChange().getDate())
                                        .cost(notificationEntity.getFieldsChange().getCost())
                                        .duration(notificationEntity.getFieldsChange().getDuration())
                                        .locationId(notificationEntity.getFieldsChange().getLocationId())
                                        .status(notificationEntity.getFieldsChange().getStatus())
                                        .build()
                        )
                        .registrationsOnEvent(notificationEntity.getRegistrations().stream().toList())
                        .isRead(notificationEntity.isReady())
                        .createdAt(notificationEntity.getCreatedAt())
                        .build())
                .toList();
    }
}
