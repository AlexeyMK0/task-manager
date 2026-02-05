package org.example;

import org.example.Exceptions.TaskNotFoundException;
import org.example.Exceptions.TaskOperationException;
import org.example.Mapping.TaskMapper;
import org.example.Operations.*;

import java.util.List;

public class DefaultTaskService implements TaskService {

    private final TaskRepository repository;

    private final int userMaxTasks;

    public DefaultTaskService(TaskRepository repository, int userMaxTasks) {
        this.repository = repository;
        this.userMaxTasks = userMaxTasks;
    }

    @Override
    public GetTask.Response getTask(GetTask.Request request) {
        Task task = repository.getTaskById(request.taskId())
                .orElseThrow(() -> new TaskNotFoundException(request.taskId()));
        return new GetTask.Response.Success(TaskMapper.MapToDto(task));
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

        Task task = repository.getTaskById(request.taskId())
                .orElseThrow(() -> new TaskNotFoundException(request.taskId()));
        if (task.status() == Status.DONE) {
            throw new TaskOperationException("Cannot update task. Status: " + task.status());
        }

        // Check if request starts the task
        if (task.status() != Status.IN_PROGRESS && status == Status.IN_PROGRESS) {
            assertCanStartTask(task);
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
        Task task = repository.getTaskById(request.taskId())
                .orElseThrow(() -> new TaskNotFoundException(request.taskId()));
        repository.delete(task);
        return new DeleteTask.Response.Success();
    }

    @Override
    public StartTask.Response startTask(StartTask.Request request) {
        Task task = repository.getTaskById(request.taskId())
                .orElseThrow(() -> new TaskNotFoundException(request.taskId()));
        if (task.status() == Status.IN_PROGRESS) {
            return new StartTask.Response.Success();
        }

        assertCanStartTask(task);

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

    private void assertCanStartTask(Task taskToStart) {
        if (taskToStart.assignedUserId() == null) {
            throw new TaskOperationException("Cannot start task with id=" + taskToStart.id() + " -- user is not assigned");
        }
        if (taskToStart.status() != Status.CREATED) {
            throw new TaskOperationException("Cannot start task with id=" + taskToStart.id() + " -- task status is " + taskToStart.status().name());
        }
        List<Task> tasksOfUser = repository.getAllTasksByAssignedUserIdAndStatus(
                taskToStart.assignedUserId(), Status.IN_PROGRESS);
        if (tasksOfUser.size() >= userMaxTasks) {
            throw new TaskOperationException("Cannot start task for user with id=" + taskToStart.assignedUserId() + " -- already has " + userMaxTasks + " tasks");
        }
    }
}
