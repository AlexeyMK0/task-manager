package org.example;

import org.example.Dto.TaskDto;
import org.example.Operations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        GetTask.Response response = taskService.getTask(new GetTask.Request(id));

        return switch (response) {
            case GetTask.Response.Success success -> ResponseEntity.ok(success.task);
            case GetTask.Response.Failure failure ->
                    ResponseEntity.badRequest().header("reason", failure.reason).build();
            default -> throw new IllegalArgumentException("unreachable state");
        };
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        logger.info("Getting all tasks");
        GetAllTasks.Response response = taskService.getAllTasks();

        return switch (response) {
            case GetAllTasks.Response.Success success -> ResponseEntity.ok(success.tasks);
            case GetAllTasks.Response.Failure failure ->
                    ResponseEntity.badRequest().header("reason", failure.reason).build();
            default -> throw new IllegalArgumentException("unreachable state");
        };
    }

    @PostMapping
    public ResponseEntity<TaskDto> createNewTask(
            @RequestBody CreateTask.Request taskToCreate) {
        logger.info("Called createTask");
        CreateTask.Response response = taskService.createTask(taskToCreate);
        return switch (response) {
            case CreateTask.Response.Success success ->
                    ResponseEntity.status(HttpStatus.CREATED).body(success.createdTask);
            case CreateTask.Response.Failure failure ->
                    ResponseEntity.badRequest().header("reason", failure.reason).build();
            default -> throw new IllegalArgumentException("unreachable state");
        };
    }

    @PutMapping
    public ResponseEntity<TaskDto> updateTask(
            @RequestBody UpdateTask.Request taskToUpdate) {
        logger.info("Called updateTask");
        UpdateTask.Response response = taskService.updateTask(taskToUpdate);
        return switch (response) {
            case UpdateTask.Response.Success success -> ResponseEntity.ok(success.updatedTask);
            case UpdateTask.Response.Failure failure ->
                    ResponseEntity.badRequest().header("reason", failure.reason).build();
            default -> throw new IllegalArgumentException("unreachable state");
        };
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable("taskId") long id) {
        logger.info("Called deleteTask");
        DeleteTask.Response response = taskService.deleteTask(new DeleteTask.Request(id));
        return switch (response) {
            case DeleteTask.Response.Success _ -> ResponseEntity.ok().build();
            case DeleteTask.Response.Failure failure ->
                    ResponseEntity.badRequest().header("reason", failure.reason).build();
            default -> throw new IllegalArgumentException("unreachable state");
        };
    }

    @PostMapping("/{taskId}/start")
    public ResponseEntity<Void> startTask(
            @PathVariable("taskId") long id) {
        logger.info("Called startTask with taskId=" + id);
        StartTask.Response response = taskService.startTask(new StartTask.Request(id));
        return switch (response) {
            case StartTask.Response.Success _ -> ResponseEntity.ok().build();
            case StartTask.Response.Failure failure ->
                    ResponseEntity.badRequest().header("reason", failure.reason).build();
            default -> throw new IllegalArgumentException("unreachable state");
        };
    }
}
