package com.taskflow.task.web.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

// Captures incoming data for POST requests

@Getter
@Setter
public class CreateTaskRequest {

    @NotBlank
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
