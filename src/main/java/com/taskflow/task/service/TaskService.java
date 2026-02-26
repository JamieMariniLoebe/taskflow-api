package com.taskflow.task.service;

import com.taskflow.common.exception.TaskNotFoundException;
import com.taskflow.task.mapper.TaskMapper;
import com.taskflow.task.persistence.TaskEntity;
import com.taskflow.task.persistence.TaskRepository;
import com.taskflow.task.web.dto.UpdateTaskRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    /*
    -- Create and save a new task
    -- Return the newly created task entity
     */
    public TaskEntity saveTask(TaskEntity task) {
        return taskRepository.save(task);
    }

    // Retrieve all tasks
    public List<TaskEntity> findAll() {
        return taskRepository.findAll();
    }

    // Retrieve a task by id, verify it exists first
    public Optional<TaskEntity> findById(Long id) {
        return taskRepository.findById(id);
    }

    // Replace an existing task, verify it exists first
    public TaskEntity replaceTask(Long id, UpdateTaskRequest newTask) {
        TaskEntity existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        existingTask.setTitle(newTask.getTitle());
        existingTask.setDescription(newTask.getDescription());
        existingTask.setStatus(newTask.getStatus());
        existingTask.setPriority(newTask.getPriority());
        existingTask.setDeadline(newTask.getDeadline());
        existingTask.setAssignee(newTask.getAssignee());
        existingTask.setUpdatedOn(LocalDateTime.now());

        return taskRepository.save(existingTask);
    }

    // Update an existing task, verify it exists first
    public TaskEntity patchTask(Long id, UpdateTaskRequest task) {

        TaskEntity origTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskMapper.updateEntity(task, origTask);
        return taskRepository.save(origTask);
    }

    // Delete task by id
    // First verify task existence (via ID)
    public String deleteTask(Long id) {
        if(!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
        return "SUCCESS: Task with id " + id + " has been deleted.";
    }
}
