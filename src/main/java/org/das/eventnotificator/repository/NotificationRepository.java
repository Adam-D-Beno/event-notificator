package org.das.eventnotificator.repository;

import org.das.eventnotificator.model.entity.EventNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<EventNotificationEntity, Long> {


    @Query("""
    select en from EventNotificationEntity en
        where en.userRegistrationsOnEvent in :userId
    """)
    List<EventNotificationEntity> findAllNotReadyUserNotifications(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("""
        update EventNotificationEntity en
        set en.isRead=true
        where en.id in :notificationIds
    """)
    int marNotificationAsRead(@Param("notificationIds") List<Long> notificationIds);
}
