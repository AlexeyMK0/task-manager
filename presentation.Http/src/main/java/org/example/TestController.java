package org.example;

import org.example.Dto.TaskDto;
import org.example.Operations.GetAllTasks;
import org.example.Operations.GetTaskById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class TestController {

    private final TaskService taskService;

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    public TestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public TaskDto getTaskById(
            @PathVariable("id") long id) {
        logger.info("Getting task by id=" + id);
        GetTaskById.Response response = taskService.getTaskById(id);

        if (response instanceof GetTaskById.Response.Success success) {
            return success.task;
        }

        throw new NoSuchElementException("Task with id=" + id + " not found");
    }

    @GetMapping
    public List<TaskDto> getAllTasks() {
        logger.info("Getting all tasks");
        GetAllTasks.Response response = taskService.getAllTasks();

        if (response instanceof GetAllTasks.Response.Success success) {
            return success.tasks;
        }

        throw new RuntimeException("Error while getting all tasks");
    }
}
