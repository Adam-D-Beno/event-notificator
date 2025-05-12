package org.das.eventnotificator.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.das.eventnotificator.model.EventFieldGeneric;
import org.das.eventnotificator.model.EventStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "fields_Change")
public class EventFieldsChangeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oldValue", column = @Column(name = "old_name")),
            @AttributeOverride(name = "newValue", column = @Column(name = "new_name"))
    })
    private EventFieldGeneric<String> name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oldValue", column = @Column(name = "old_max_places")),
            @AttributeOverride(name = "newValue", column = @Column(name = "new_max_places"))
    })
    private EventFieldGeneric<Integer> maxPlaces;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oldValue", column = @Column(name = "old_date_event")),
            @AttributeOverride(name = "newValue", column = @Column(name = "new_date_event"))
    })
    private EventFieldGeneric<LocalDateTime> date;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oldValue", column = @Column(name = "old_cost")),
            @AttributeOverride(name = "newValue", column = @Column(name = "new_cost"))
    })
    private EventFieldGeneric<BigDecimal> cost;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oldValue", column = @Column(name = "old_duration")),
            @AttributeOverride(name = "newValue", column = @Column(name = "new_duration"))
    })
    private EventFieldGeneric<Integer> duration;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oldValue", column = @Column(name = "old_location_id")),
            @AttributeOverride(name = "newValue", column = @Column(name = "new_location_id"))
    })
    private EventFieldGeneric<Long> locationId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oldValue", column = @Column(name = "old_status")),
            @AttributeOverride(name = "newValue", column = @Column(name = "new_status"))
    })
    @Enumerated(EnumType.STRING)
    private EventFieldGeneric<EventStatus> status;

    @OneToOne(cascade = CascadeType.REMOVE)
    private NotificationEntity notification;

}
