package com.taskflow.task.web;

import com.taskflow.task.mapper.TaskMapper;
import com.taskflow.task.persistence.TaskEntity;
import com.taskflow.task.service.TaskService;
import com.taskflow.task.web.dto.CreateTaskRequest;
import com.taskflow.task.web.dto.TaskResponse;
import com.taskflow.task.web.dto.UpdateTaskRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks") // base point for all endpoints in this controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskMapper taskMapper;

    /*
    -- Endpoint to get all tasks
    -- Maps each entity to response DTO
    -- Returns list of DTOs to client using Java Streams
     */
    @GetMapping()
    public List<TaskResponse> getAllTasks() {
        List<TaskEntity> listOfRecords = taskService.findAll();
        return listOfRecords.stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    /*
    -- Endpoint to get a specific task by ID
    -- Takes in path variable for task ID
    -- Retrieves entity via service
    -- Maps entity to response DTO
    -- Throws 404 if task not found
    */
    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        Optional<TaskEntity> retrievedTask = taskService.findById(id);
        return retrievedTask
                .map(taskMapper::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with id " + id + " not found."));
    }

    /*
    --Endpoint to create a new task
    --Takes in DTO for new create request
    --Validates incoming request body
    -- Maps DTO to entity
    --Saves entity via service
    -- Returns DTO response for newly created task to client
    */
    @PostMapping()
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskEntity entity = taskMapper.toEntity(request);
        TaskEntity newEntity = taskService.saveTask(entity);
        return taskMapper.toResponse(newEntity);
    }

    /*
    -- Endpoint to replace an existing task
    -- Takes in path variable for task ID and request body for new task data
    -- Maps request body to entity
    -- Calls service to replace existing task
    -- Returns DTO response for updated task to client
     */
    @PutMapping("/{id}")
    public TaskResponse replaceTask(@PathVariable Long id, @RequestBody UpdateTaskRequest newTask) {
        TaskEntity updatedTask = taskService.replaceTask(id, newTask);
        return taskMapper.toResponse(updatedTask);
    }

    /*
    -- Endpoint to update an existing task
    -- Takes in path variable for task ID and request body for fields to update
    -- Calls service to patch existing task
    -- Returns DTO response for updated task to client
     */
    @PatchMapping("/{id}")
    public TaskResponse patchTask(@PathVariable Long id, @RequestBody UpdateTaskRequest task) {
        TaskEntity patchedTask = taskService.patchTask(id, task);
        return taskMapper.toResponse(patchedTask);
    }

    // Endpoint to delete an existing task
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id);
    }
}