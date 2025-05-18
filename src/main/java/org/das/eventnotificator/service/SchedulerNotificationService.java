package org.das.eventnotificator.service;

import lombok.RequiredArgsConstructor;
import org.das.eventnotificator.model.entity.NotificationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class SchedulerNotificationService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerNotificationService.class);
    private final NotificationService notificationService;
    private static final int RETENTION_DAYS = 7;


    @Scheduled(cron = "${notification.cron}")
    public void removeOldNotification() {
        log.info("Scheduled delete notification started");
        List<Long> notificationIds= notificationService.deleteNotificationByMoreDays(RETENTION_DAYS);
        log.info("Scheduled delete notification Ids={}", notificationIds);
    }
}