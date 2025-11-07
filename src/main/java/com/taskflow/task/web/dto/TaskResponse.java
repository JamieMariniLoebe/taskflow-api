package com.taskflow.task.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// Captures data sent back to clients in responses

@Getter
@Setter
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private String status;
    private String assignee;
    private LocalDateTime deadline;
    private Integer priority;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
