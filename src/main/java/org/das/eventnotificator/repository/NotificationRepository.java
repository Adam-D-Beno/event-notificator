package org.das.eventnotificator.repository;

import org.das.eventnotificator.model.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {


    @Query("""
    select DISTINCT en from NotificationEntity en join fetch en.registrations
        where :userId member of en.registrations
        AND en.isReady=false
    """)
    List<NotificationEntity> findAllNotReadyUserNotifications(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("""
        update NotificationEntity en
        set en.isReady=true
        where en.id in :notificationIds
        AND en.isReady = false
    """)
    int markNotificationAsRead(@Param("notificationIds") List<Long> notificationIds);
}
