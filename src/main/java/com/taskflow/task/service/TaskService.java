package com.taskflow.task.service;

import com.taskflow.task.persistence.TaskEntity;
import com.taskflow.task.persistence.TaskRepository;
import com.taskflow.task.web.dto.CreateTaskRequest;
import com.taskflow.task.web.dto.UpdateTaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    /*
    -- Create and save a new task
    -- Return the newly created task entity
     */
    public TaskEntity saveTask(TaskEntity task) {
        TaskEntity newTask = taskRepository.save(task);
        return newTask;
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
                .orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " does not exist."));

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
                .orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " does not exist."));

        if(task.getTitle() != null) {
            origTask.setTitle(task.getTitle());
        }
        if(task.getDescription() != null) {
            origTask.setDescription(task.getDescription());
        }
        if(task.getStatus() != null) {
            origTask.setStatus(task.getStatus());
        }
        if(task.getPriority() != null) {
            origTask.setPriority(task.getPriority());
        }
        if(task.getDeadline() != null) {
            origTask.setDeadline(task.getDeadline());
        }
        if(task.getAssignee() != null) {
            origTask.setAssignee(task.getAssignee());
        }
        origTask.setUpdatedOn(LocalDateTime.now());

        return taskRepository.save(origTask);
    }

    // Delete task by id
    // First verify task existence (via ID)
    public String deleteTask(Long id) {
        if(!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task with id " + id + " does not exist.");
        }
        taskRepository.deleteById(id);
        return "SUCCESS: Task with id " + id + " has been deleted.";
    }
}
