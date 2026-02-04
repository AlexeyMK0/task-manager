package org.example;

import org.example.Mapping.TaskMapper;
import org.example.Operations.*;

import java.util.List;
import java.util.Optional;

public class DefaultTaskService implements TaskService {

    private final TaskRepository repository;

    private final int userMaxTasks;

    public DefaultTaskService(TaskRepository repository, int userMaxTasks) {
        this.repository = repository;
        this.userMaxTasks = userMaxTasks;
    }

    @Override
    public GetTask.Response getTask(GetTask.Request request) {
        long id = request.taskId();
        Optional<Task> task = repository.getTaskById(id);
        if (task.isEmpty()) {
            return new GetTask.Response.Failure("task with taskId " + id + " not found");
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
            return new UpdateTask.Response.Failure("Task with taskId " + request.taskId() + " not found");
        }

        Task task = foundTask.get();
        if (task.status() == Status.DONE) {
            return new UpdateTask.Response.Failure("Cannot update task. Status: " + task.status());
        }

        // Check if request starts the task
        if (task.status() != Status.IN_PROGRESS && status == Status.IN_PROGRESS) {
            Optional<String> startFailureReason = isCanStartTask(task);
            if (startFailureReason.isPresent()) {
                return new UpdateTask.Response.Failure(startFailureReason.get());
            }
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
            return new DeleteTask.Response.Failure("Task with taskId " + request.taskId() + " not found");
        }
        repository.delete(foundTask.get());
        return new DeleteTask.Response.Success();
    }

    @Override
    public StartTask.Response startTask(StartTask.Request request) {
        Optional<Task> foundTask = repository.getTaskById(request.taskId());
        if (foundTask.isEmpty()) {
            return new StartTask.Response.Failure("Task with taskId " + request.taskId() + " not found");
        }
        Task task = foundTask.get();
        if (task.status() == Status.IN_PROGRESS) {
            return new StartTask.Response.Success();
        }
        Optional<String> failureReason = isCanStartTask(task);
        if (failureReason.isPresent()) {
            return new StartTask.Response.Failure(failureReason.get());
        }

        Task startedTask = new Task(
                task.id(),
                task.creatorId(),
                task.assignedUserId(),
                Status.IN_PROGRESS,
                task.createDateTime(),
                task.deadlineDateTime(),
                task.priority()
        );
        repository.update(startedTask);
        return new StartTask.Response.Success();
    }

    // returns failure reason
    // empty if task can be started
    private Optional<String> isCanStartTask(Task task) {
        if (task.assignedUserId() == null) {
            return Optional.of("Cannot start task with id=" + task.id() + " -- user is not assigned");
        }
        if (task.status() != Status.CREATED) {
            return Optional.of("Cannot start task with id=" + task.id() + " -- task status is " + task.status().name());
        }
        List<Task> tasksOfUser = repository.getAllTasksByAssignedUserIdAndStatus(
                task.assignedUserId(), Status.IN_PROGRESS);
        if (tasksOfUser.size() >= userMaxTasks) {
            return Optional.of("Cannot start task for user with id=" + task.assignedUserId() + " -- already has " + userMaxTasks + " tasks");
        }

        return Optional.empty();
    }
}
