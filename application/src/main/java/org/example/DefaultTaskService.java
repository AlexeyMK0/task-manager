package org.example;

import org.example.Mapping.TaskMapper;
import org.example.Operations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultTaskService implements TaskService {

    private final Map<Long, Task> repository = new HashMap<>();

    private final AtomicLong currentTaskId = new AtomicLong();

    @Override
    public GetTask.Response getTask(GetTask.Request request) {
        long id = request.id();
        if (!repository.containsKey(id)) {
            return new GetTask.Response.Failure("task with id " + id + " not found");
        }

        return new GetTask.Response.Success(
                TaskMapper.MapToDto(repository.get(id)));
    }

    @Override
    public GetAllTasks.Response getAllTasks() {
        return new GetAllTasks.Response.Success(
                repository.values().stream().map(TaskMapper::MapToDto).toList());
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
                currentTaskId.incrementAndGet(),
                request.creatorId(),
                request.assignedUserId(),
                Status.CREATED,
                request.createDateTime(),
                request.deadlineDateTime(),
                priority);
        repository.put(task.id(), task);
        return new CreateTask.Response.Success(TaskMapper.MapToDto(task));
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

        if (!repository.containsKey(request.taskId())) {
            return new UpdateTask.Response.Failure("Task with id " + request.taskId() + " not found");
        }

        Task task = repository.get(request.taskId());
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
        repository.put(task.id(), task);

        return new UpdateTask.Response.Success(TaskMapper.MapToDto(task));
    }

    @Override
    public DeleteTask.Response deleteTask(DeleteTask.Request request) {
        long id = request.taskId();
        if (!repository.containsKey(id)) {
            return new DeleteTask.Response.Failure("Task with id " + request.taskId() + " not found");
        }
        repository.remove(id);
        return new DeleteTask.Response.Success();
    }
}
