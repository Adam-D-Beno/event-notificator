package org.das.eventnotificator.service;

import lombok.RequiredArgsConstructor;
import org.das.eventnotificator.controller.NotificationController;
import org.das.eventnotificator.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationsService {

    private static final Logger log = LoggerFactory.getLogger(NotificationsService.class);
    private final NotificationRepository notificationRepository;
}
