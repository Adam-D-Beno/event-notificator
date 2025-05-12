package org.das.eventnotificator.repository;

import org.das.eventnotificator.model.entity.EventFieldsChangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EventFieldsChangeRepository extends JpaRepository<EventFieldsChangeEntity, Long> {

    @Transactional
    @Modifying
    @Query(value = """
        delete from event_notificator.public.fields_change fc
            where fc.id = :id
            RETURNING *
    """, nativeQuery = true)
    int deleteFieldsChangeById(@Param("id") Long id);
}
