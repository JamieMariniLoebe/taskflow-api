package com.taskflow.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

/**
 * Overrides Spring Boot's default Kafka consumer configuration
 * with manual acknowledgement and retry/DLQ error handling
 */

@Configuration
public class KafkaConfig {

    /** Creates consumer container and sets manual acknowledgement and retry/DLQ error handling
     *
     * @param consumerFactory Creates consumer instances
     * @param defaultErrorHandler Error handler set to retry/DLQ
     * @return Consumer container configured with manual ack and retry/DLQ error handling
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaskEvent> listenerContainerFactory(ConsumerFactory<String, TaskEvent> consumerFactory, DefaultErrorHandler defaultErrorHandler)
    {
        ConcurrentKafkaListenerContainerFactory<String, TaskEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(defaultErrorHandler);

        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        return factory;
    }

    /** Creates error handler configured with retry/DLQ error handling
     *
     * @param kafkaTemplate Used to publish failed messages to the dead letter topic
     * @return Error handler to process retries/DLQ
     */
    @Bean
    public DefaultErrorHandler defaultHandler(KafkaTemplate<String, TaskEvent> kafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);

        FixedBackOff backOff =  new FixedBackOff(1000, 3);

        return new DefaultErrorHandler(recoverer, backOff);
    }
}
