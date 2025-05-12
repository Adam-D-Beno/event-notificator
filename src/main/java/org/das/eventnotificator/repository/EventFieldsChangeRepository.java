package org.das.eventnotificator.repository;

import org.das.eventnotificator.model.entity.EventFieldsChangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventFieldsChangeRepository extends JpaRepository<EventFieldsChangeEntity, Long> {
}
