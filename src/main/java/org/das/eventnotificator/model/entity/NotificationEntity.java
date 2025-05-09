package org.das.eventnotificator.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "notification")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "modifier_id")
    private Long modifierById;

    @Column(name = "owner_event_id")
    private Long ownerEventId;

    @OneToOne
    @JoinColumn(name = "Fields_Change_id")
    private EventFieldsChangeEntity eventFieldsChangeEntity;

    @ElementCollection
    @CollectionTable(name = "event_registrations",
            joinColumns = @JoinColumn(name = "notification_id"))
    @Column(name = "user_id")
    List<Long> registrationsOnEvent;

    @Column(name = "is_ready")
    private boolean isReady;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;
}
