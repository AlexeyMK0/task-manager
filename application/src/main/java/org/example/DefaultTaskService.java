package org.example;

import org.example.Mapping.TaskMapper;
import org.example.Operations.*;

import java.util.Optional;

public class DefaultTaskService implements TaskService {

    private final TaskRepository repository;

    public DefaultTaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public GetTask.Response getTask(GetTask.Request request) {
        long id = request.id();
        Optional<Task> task = repository.getTaskById(id);
        if (task.isEmpty()) {
            return new GetTask.Response.Failure("task with id " + id + " not found");
        }
        return new GetTask.Response.Success(TaskMapper.MapToDto(task.get()));
    }

    @Override
    public GetAllTasks.Response getAllTasks() {
        return new GetAllTasks.Response.Success(
                repository.getAllTasks().stream().map(TaskMapper::MapToDto).toList());
    }

    @Override
    public CreateTask.Response createTask(CreateTask.Request request) {
        Priority priority;
        try {
            priority = Priority.valueOf(request.importance());
        } catch (IllegalArgumentException e) {
            return new CreateTask.Response.Failure("Unknown importance level: " + request.importance());
        }

        Task task = new Task(
                null,
                request.creatorId(),
                request.assignedUserId(),
                Status.CREATED,
                request.createDateTime(),
                request.deadlineDateTime(),
                priority);
        Task createdTask = repository.create(task);
        return new CreateTask.Response.Success(TaskMapper.MapToDto(createdTask));
    }

    public UpdateTask.Response updateTask(UpdateTask.Request request) {
        Status status;
        try {
            status = Status.valueOf(request.status());
        } catch (IllegalArgumentException e) {
            return new UpdateTask.Response.Failure("Unknown status " + request.status());
        }

        Priority priority;
        try {
            priority = Priority.valueOf(request.importance());
        } catch (IllegalArgumentException e) {
            return new UpdateTask.Response.Failure("Unknown importance level " + request.importance());
        }

        Optional<Task> foundTask = repository.getTaskById(request.taskId());
        if (foundTask.isEmpty()) {
            return new UpdateTask.Response.Failure("Task with id " + request.taskId() + " not found");
        }

        Task task = foundTask.get();
        if (task.status() == Status.DONE) {
            return new UpdateTask.Response.Failure("Cannot update task. Status: " + task.status());
        }

        task = new Task(
                task.id(),
                request.creatorId(),
                request.assignedUserId(),
                status,
                request.createDateTime(),
                request.deadlineDateTime(),
                priority);
        task = repository.update(task);

        return new UpdateTask.Response.Success(TaskMapper.MapToDto(task));
    }

    @Override
    public DeleteTask.Response deleteTask(DeleteTask.Request request) {
        Optional<Task> foundTask = repository.getTaskById(request.taskId());
        if (foundTask.isEmpty()) {
            return new DeleteTask.Response.Failure("Task with id " + request.taskId() + " not found");
        }
        repository.delete(foundTask.get());
        return new DeleteTask.Response.Success();
    }
}
