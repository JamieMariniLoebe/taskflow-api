package com.taskflow.task.service;

import com.taskflow.common.exception.TaskNotFoundException;
import com.taskflow.messaging.TaskEvent;
import com.taskflow.messaging.TaskEventAction;
import com.taskflow.messaging.TaskEventProducer;
import com.taskflow.task.mapper.TaskMapper;
import com.taskflow.task.persistence.TaskEntity;
import com.taskflow.task.persistence.TaskRepository;
import com.taskflow.task.web.dto.UpdateTaskRequest;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskEventProducer taskEventProducer;
    private final Counter taskCounter;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, TaskEventProducer taskEventProducer, MeterRegistry meterRegistry) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.taskEventProducer = taskEventProducer;

        this.taskCounter = meterRegistry.counter("tasks.creation");
        Gauge.builder("tasks.existing", taskRepository, TaskRepository::count)
                .register(meterRegistry);
    }

    /*
    -- Create and save a new task
    -- Return the newly created task entity
     */
    public TaskEntity createTask(TaskEntity task) {
        TaskEntity finalTask = taskRepository.save(task);
        TaskEvent taskEvent = new TaskEvent(finalTask.getId(), TaskEventAction.CREATED, finalTask.getAssignee(), finalTask.getCreatedOn().atZone(ZoneId.systemDefault()).toInstant());
        taskEventProducer.publish(taskEvent);

        taskCounter.increment();

        return finalTask;
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

        existingTask = taskRepository.save(existingTask);

        TaskEvent taskEvent = new TaskEvent(existingTask.getId(), TaskEventAction.UPDATED, existingTask.getAssignee(), existingTask.getCreatedOn().atZone(ZoneId.systemDefault()).toInstant());
        taskEventProducer.publish(taskEvent);

        return existingTask;
    }

    // Update an existing task, verify it exists first
    public TaskEntity patchTask(Long id, UpdateTaskRequest task) {

        TaskEntity origTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskMapper.updateEntity(task, origTask);

        TaskEntity newTask = taskRepository.save(origTask);

        TaskEvent taskEvent = new TaskEvent(newTask.getId(), TaskEventAction.UPDATED, newTask.getAssignee(), newTask.getCreatedOn().atZone(ZoneId.systemDefault()).toInstant());
        taskEventProducer.publish(taskEvent);

        return newTask;
    }

    // Delete task by id
    // First verify task existence (via ID)
    public String deleteTask(Long id) {
        TaskEntity myTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        TaskEvent taskEvent = new TaskEvent(myTask.getId(), TaskEventAction.DELETED, myTask.getAssignee(), myTask.getCreatedOn().atZone(ZoneId.systemDefault()).toInstant());

        taskRepository.deleteById(id);

        taskEventProducer.publish(taskEvent);

        return "SUCCESS: Task with id " + id + " has been deleted.";
    }
}
