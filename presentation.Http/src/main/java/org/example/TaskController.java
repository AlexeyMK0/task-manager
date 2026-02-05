package org.example;

import jakarta.validation.Valid;
import org.example.Dto.TaskDto;
import org.example.Operations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tasks")
public class TaskController {

    private final TaskService taskService;

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTask(
            @PathVariable("taskId") long id) {
        logger.info("Getting task by taskId=" + id);
        TaskDto response = taskService.getTask(new GetTask.Request(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        logger.info("Getting all tasks");
        List<TaskDto> response = taskService.getAllTasks();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TaskDto> createNewTask(
            @Valid @RequestBody CreateTask.Request taskToCreate) {
        logger.info("Called createTask");
        TaskDto response = taskService.createTask(taskToCreate);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<TaskDto> updateTask(
            @Valid @RequestBody UpdateTask.Request taskToUpdate) {
        logger.info("Called updateTask");
        TaskDto response = taskService.updateTask(taskToUpdate);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable("taskId") long id) {
        logger.info("Called deleteTask");
        taskService.deleteTask(new DeleteTask.Request(id));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{taskId}/start")
    public ResponseEntity<Void> startTask(
            @PathVariable("taskId") long id) {
        logger.info("Called startTask with taskId=" + id);
        taskService.startTask(new StartTask.Request(id));
        return ResponseEntity.ok().build();
    }
}
