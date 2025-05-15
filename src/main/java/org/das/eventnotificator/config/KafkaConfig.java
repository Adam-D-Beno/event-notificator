package org.das.eventnotificator.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.das.eventnotificator.model.EventChangeKafkaMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public ConsumerFactory<Long, EventChangeKafkaMessage> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "notificator-group");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        var factory = new DefaultKafkaConsumerFactory<Long, EventChangeKafkaMessage>(configProps);
        factory.setKeyDeserializer(new ErrorHandlingDeserializer<>(new LongDeserializer()));
        factory.setValueDeserializer(new ErrorHandlingDeserializer<>(
                new JsonDeserializer<>(EventChangeKafkaMessage.class, false)
        ));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, EventChangeKafkaMessage> containerFactory(
            ConsumerFactory<Long, EventChangeKafkaMessage> consumerFactory
    ) {
            var factory = new ConcurrentKafkaListenerContainerFactory<Long, EventChangeKafkaMessage>();
            factory.setConsumerFactory(consumerFactory);

            return factory;
    }
}
