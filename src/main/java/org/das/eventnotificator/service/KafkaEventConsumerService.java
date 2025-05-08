package org.das.eventnotificator.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.das.eventnotificator.model.EventChangeKafkaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventConsumerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventConsumerService.class);

    @KafkaListener(topics = "${event-topic}", containerFactory = "containerFactory")
    public void listenChangeEvents(ConsumerRecord<Long, EventChangeKafkaMessage> eventChangeRecord) {
        log.info("Get event change from kafka: eventChange = {}", eventChangeRecord.value());

    }
}
