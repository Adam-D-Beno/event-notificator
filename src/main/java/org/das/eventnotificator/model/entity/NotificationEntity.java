package org.das.eventnotificator.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "modifier_id")
    private Long modifierById;

    @Column(name = "owner_event_id", nullable = false)
    private Long ownerEventId;

    @OneToOne
    @JoinColumn(name = "fields_Change_id")
    private EventFieldsChangeEntity eventFieldsChangeEntity;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "event_registrations",
            joinColumns = @JoinColumn(name = "notification_id"),
            foreignKey = @ForeignKey(name = "fk_notification"))
    private Set<Long> registrations = new HashSet<>();

    @Column(name = "is_ready", nullable = false)
    private boolean isReady;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
