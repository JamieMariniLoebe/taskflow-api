package com.taskflow.task.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// Captures partial updates for PATCH/PUT

@Getter
@Setter
public class UpdateTaskRequest {

    // Added validation annotation
    @Size(max = 1000)
    private String title;

    @Size(max = 1000)
    private String description;

    @Size(max = 50)
    private String status;

    @Size(max = 100)
    private String assignee;

    private LocalDateTime deadline;

    @Min(1)
    private Integer priority;
}
