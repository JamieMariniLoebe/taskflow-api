package com.taskflow.messaging;

/**
 * Defines the set of lifecycle actions that can be performed on a task
 */

public enum TaskEventAction {
    CREATED,
    UPDATED,
    COMPLETED,
    DELETED
}
