package com.taskflow.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Publishes TaskEvent object to 'task-events' Kafka topic
 */

@Component
public class TaskEventProducer {

    private static final String TOPIC = "task-events";

    private final KafkaTemplate<String, TaskEvent> kafkaTemplate;

     public TaskEventProducer(KafkaTemplate<String, TaskEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(TaskEvent message) {
         kafkaTemplate.send(TOPIC, (message.taskId()).toString(), message);
    }

}
