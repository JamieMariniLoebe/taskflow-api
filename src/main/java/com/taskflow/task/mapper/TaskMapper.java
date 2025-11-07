package com.taskflow.task.mapper;

import com.taskflow.task.persistence.TaskEntity;
import com.taskflow.task.web.dto.CreateTaskRequest;
import com.taskflow.task.web.dto.TaskResponse;
import com.taskflow.task.web.dto.UpdateTaskRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskMapper {

    // Convert CreateTaskRequest DTO to TaskEntity
    public TaskEntity toEntity(CreateTaskRequest request) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(request.getTitle());
        taskEntity.setDescription(request.getDescription());
        taskEntity.setStatus(request.getStatus());
        taskEntity.setAssignee(request.getAssignee());
        taskEntity.setDeadline(request.getDeadline());
        taskEntity.setPriority(request.getPriority());
        taskEntity.setCreatedOn(LocalDateTime.now());
        return taskEntity;
    }

    // Convert TaskEntity to TaskResponse DTO
    public TaskResponse toResponse(TaskEntity entity) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(entity.getId());
        taskResponse.setTitle(entity.getTitle());
        taskResponse.setDescription(entity.getDescription());
        taskResponse.setStatus(entity.getStatus());
        taskResponse.setAssignee(entity.getAssignee());
        taskResponse.setDeadline(entity.getDeadline());
        taskResponse.setPriority(entity.getPriority());
        taskResponse.setCreatedOn(entity.getCreatedOn());
        taskResponse.setUpdatedOn(entity.getUpdatedOn());
        return taskResponse;
    }

    // Update existing TaskEntity from UpdateTaskRequest (for PATCH)
    public void updateEntity(UpdateTaskRequest request, TaskEntity entity) {
        if( request.getTitle() != null) {
            entity.setTitle(request.getTitle());
        }

        if( request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }

        if( request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }

        if( request.getAssignee() != null) {
            entity.setAssignee(request.getAssignee());
        }

        if( request.getDeadline() != null) {
            entity.setDeadline(request.getDeadline());
        }

        if( request.getPriority() != null) {
            entity.setPriority(request.getPriority());
        }

        entity.setUpdatedOn(LocalDateTime.now());
    }
}