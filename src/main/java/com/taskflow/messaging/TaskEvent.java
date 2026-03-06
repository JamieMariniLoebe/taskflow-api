package com.taskflow.messaging;

import java.time.Instant;

/**
 * Carries the payload that flows through the 'task-events' Kafka topic
 */

public record TaskEvent (Long taskId, TaskEventAction action, String userId, Instant createdOn) { }