package org.das.eventnotificator.repository;

import org.das.eventnotificator.model.entity.EventChangeNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<EventChangeNotificationEntity, Long> {
}
