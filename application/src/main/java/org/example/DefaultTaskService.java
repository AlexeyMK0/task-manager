package org.example;

import org.example.Dto.TaskDto;
import org.example.Exceptions.TaskNotFoundException;
import org.example.Exceptions.TaskOperationException;
import org.example.Mapping.ImportanceMapper;
import org.example.Mapping.StatusMapper;
import org.example.Mapping.TaskMapper;
import org.example.Operations.*;

import java.time.LocalDateTime;
import java.util.List;

public class DefaultTaskService implements TaskService {

    private final TaskRepository repository;

    private final int userMaxTasks;

    public DefaultTaskService(TaskRepository repository, int userMaxTasks) {
        this.repository = repository;
        this.userMaxTasks = userMaxTasks;
    }

    @Override
    public TaskDto getTask(GetTask.Request request) {
        Task task = repository.getTaskById(request.taskId())
                .orElseThrow(() -> new TaskNotFoundException(request.taskId()));
        return TaskMapper.MapToDto(task);
    }

    @Override
    public List<TaskDto> getAllTasks() {
        return repository.getAllTasks().stream().map(TaskMapper::MapToDto).toList();
    }

    @Override
    public TaskDto createTask(CreateTask.Request request) {
        Priority priority = ImportanceMapper.INSTANCE.toDomain(request.importance());

        Task task = new Task(
                null,
                request.creatorId(),
                request.assignedUserId(),
                Status.CREATED,
                request.createDateTime(),
                request.deadlineDateTime(),
                priority,
                null);
        Task createdTask = repository.create(task);
        return TaskMapper.MapToDto(createdTask);
    }

    public TaskDto updateTask(UpdateTask.Request request) {
        Status status = StatusMapper.INSTANCE.toDomain(request.status());
        Priority priority = ImportanceMapper.INSTANCE.toDomain(request.importance());

        Task task = repository.getTaskById(request.taskId())
                .orElseThrow(() -> new TaskNotFoundException(request.taskId()));
        if (task.status() == Status.DONE && status != Status.IN_PROGRESS) {
            throw new TaskOperationException("Cannot update task. Status: " + task.status());
        }

        // Check if request starts the task
        if (task.status() != Status.IN_PROGRESS && status == Status.IN_PROGRESS) {
            assertCanStartTask(task);
        }

        LocalDateTime doneDateTime = null;
        if (status == Status.DONE) {
            assertCanFinishTask(task);
            doneDateTime = LocalDateTime.now();
        }

        task = new Task(
                task.id(),
                request.creatorId(),
                request.assignedUserId(),
                status,
                request.createDateTime(),
                request.deadlineDateTime(),
                priority,
                doneDateTime);
        task = repository.update(task);

        return TaskMapper.MapToDto(task);
    }

    @Override
    public void deleteTask(DeleteTask.Request request) {
        Task task = repository.getTaskById(request.taskId())
                .orElseThrow(() -> new TaskNotFoundException(request.taskId()));
        repository.delete(task);
    }

    @Override
    public void startTask(StartTask.Request request) {
        Task task = repository.getTaskById(request.taskId())
                .orElseThrow(() -> new TaskNotFoundException(request.taskId()));
        if (task.status() == Status.IN_PROGRESS) {
            return;
        }

        assertCanStartTask(task);

        var startedTask = new Task(
                task.id(),
                task.creatorId(),
                task.assignedUserId(),
                Status.IN_PROGRESS,
                task.createDateTime(),
                task.deadlineDateTime(),
                task.priority(),
                null);
        repository.update(startedTask);
    }

    @Override
    public void completeTask(CompleteTask.Request request) {
        Task task = repository.getTaskById(request.taskId())
                .orElseThrow(() -> new TaskNotFoundException(request.taskId()));
        if (task.status() == Status.DONE) {
            return;
        }

        assertCanFinishTask(task);

        var finishedTask = new Task(
                task.id(),
                task.creatorId(),
                task.assignedUserId(),
                Status.DONE,
                task.createDateTime(),
                task.deadlineDateTime(),
                task.priority(),
                LocalDateTime.now()
        );
        repository.update(finishedTask);
    }

    private void assertCanStartTask(Task taskToStart) {
        if (taskToStart.assignedUserId() == null) {
            throw new TaskOperationException("Cannot start task with id=" + taskToStart.id() + " -- user is not assigned");
        }

        long numberOfTasks = repository.countTasksByAssignedUserIdAndStatus(
                taskToStart.assignedUserId(), Status.IN_PROGRESS);
        if (numberOfTasks >= userMaxTasks) {
            throw new TaskOperationException("Cannot start task for user with id=" + taskToStart.assignedUserId() + " -- already has " + userMaxTasks + " tasks");
        }
    }

    private void assertCanFinishTask(Task taskToFinish) {
        if (taskToFinish.assignedUserId() == null) {
            throw new TaskOperationException("Cannot finish task with id=" + taskToFinish.id() + " -- user is not assigned");
        }
    }
}
