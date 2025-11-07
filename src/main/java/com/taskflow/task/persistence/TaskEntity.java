package com.taskflow.task.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class TaskEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private String status; // e.g., "Pending", "In Progress", "Completed"
    private String assignee;
    private LocalDateTime deadline;
    private Integer priority;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

}
