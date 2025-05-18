package org.das.eventnotificator.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.das.eventnotificator.model.EventChangeKafkaMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public ConsumerFactory<Long, EventChangeKafkaMessage> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:19093,localhost:19094");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "notificator-group");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        var factory = new DefaultKafkaConsumerFactory<Long, EventChangeKafkaMessage>(configProps);
        factory.setKeyDeserializer(new ErrorHandlingDeserializer<>(new LongDeserializer()));
        factory.setValueDeserializer(new ErrorHandlingDeserializer<>(
                new JsonDeserializer<>(EventChangeKafkaMessage.class, false)
        ));
        return factory;
    }

    @Bean
    public ProducerFactory<Long, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:19093,localhost:19094");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<Long, Object> kafkaTemplate(ProducerFactory<Long, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(KafkaTemplate<Long, Object> kafkaTemplate) {
        return new DeadLetterPublishingRecoverer(kafkaTemplate, (record, exception) ->
                new TopicPartition(record.topic() + ".DLT", record.partition()));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, EventChangeKafkaMessage> containerFactory(
            ConsumerFactory<Long, EventChangeKafkaMessage> consumerFactory,
            DeadLetterPublishingRecoverer deadLetterPublishingRecoverer
    ) {
            var containerFactory = new ConcurrentKafkaListenerContainerFactory<Long, EventChangeKafkaMessage>();
            containerFactory.setConsumerFactory(consumerFactory);
            containerFactory.setCommonErrorHandler(
                    new DefaultErrorHandler(
                            deadLetterPublishingRecoverer,
                            new FixedBackOff(1000L, 3))
            );
            return containerFactory;
    }
}
