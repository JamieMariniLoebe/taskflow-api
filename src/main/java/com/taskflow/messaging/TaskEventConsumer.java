package com.taskflow.messaging;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Consumes TaskEvent object and manually acknowledges upon completion
 */

@Component
public class TaskEventConsumer {

    private final Logger logger = LoggerFactory.getLogger(TaskEventConsumer.class);

    @KafkaListener(topics = "task-events", groupId = "taskflow-event-processor")
    public void consume(ConsumerRecord<String, TaskEvent> message, Acknowledgment ack) {
        logger.info("Consumed message: {} ", message);

        ack.acknowledge();
    }
}
