package org.das.eventnotificator.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.das.eventnotificator.model.EventFieldChange;
import org.das.eventnotificator.model.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notification")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class EventNotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "user_changed_id", nullable = false)
    private Long userEventChangedId;

    @Column(name = "owner_event_id", nullable = false)
    private Long ownerEventId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oldValue", column = @Column(name = "old_name")),
            @AttributeOverride(name = "newValue", column = @Column(name = "new_name"))
    })
    private EventFieldChange<String> name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oldValue", column = @Column(name = "old_maxPlaces")),
            @AttributeOverride(name = "newValue", column = @Column(name = "new_maxPlaces"))
    })
    private EventFieldChange<Integer> maxPlaces;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oldValue", column = @Column(name = "old_date_event")),
            @AttributeOverride(name = "newValue", column = @Column(name = "new_date_event"))
    })
    private EventFieldChange<LocalDateTime> date;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oldValue", column = @Column(name = "old_cost")),
            @AttributeOverride(name = "newValue", column = @Column(name = "new_cost"))
    })
    private EventFieldChange<BigDecimal> cost;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oldValue", column = @Column(name = "old_duration")),
            @AttributeOverride(name = "newValue", column = @Column(name = "new_duration"))
    })
    private EventFieldChange<Integer> duration;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oldValue", column = @Column(name = "old_locationId")),
            @AttributeOverride(name = "newValue", column = @Column(name = "new_locationId"))
    })
    private EventFieldChange<Long> locationId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oldValue", column = @Column(name = "old_event_status")),
            @AttributeOverride(name = "newValue", column = @Column(name = "new_event_status"))
    })
    private EventFieldChange<EventStatus> status;

    @Column(name = "users")
    List<Long> userRegistrationsOnEvent;

    @Column(name = "is_ready", nullable = false)
    private boolean isRead;

    @Column(name = "notification_date", nullable = false)
    private LocalDateTime notificationDate;
}
