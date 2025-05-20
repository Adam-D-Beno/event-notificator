package org.das.eventnotificator.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.das.eventnotificator.model.EventChangeKafkaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaEventConsumerService {

    private final NotificationService notificationService;
    private static final Logger log = LoggerFactory.getLogger(KafkaEventConsumerService.class);


    @KafkaListener(topics = "change-event-topic", containerFactory = "containerFactory")
    public void listenChangeEvents(
            ConsumerRecord<Long, EventChangeKafkaMessage> eventChangeRecord,
            Acknowledgment acknowledgment
    ) {
        log.info("Begin Receive event change from kafka: eventChange={}, topic={}, partition={}",
        eventChangeRecord.value(), eventChangeRecord.topic(), eventChangeRecord.partition());
        notificationService.save(eventChangeRecord.value());
        acknowledgment.acknowledge();
        log.info("Received event change from kafka: eventChange={}, topic={}, partition={}",
                    eventChangeRecord.value(), eventChangeRecord.topic(), eventChangeRecord.partition());
    }
}
