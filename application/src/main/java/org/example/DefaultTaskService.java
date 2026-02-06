package org.example;

import org.example.Dto.TaskDto;
import org.example.Exceptions.TaskNotFoundException;
import org.example.Exceptions.TaskOperationException;
import org.example.Mapping.ImportanceMapper;
import org.example.Mapping.StatusMapper;
import org.example.Mapping.TaskMapper;
import org.example.Operations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class DefaultTaskService implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTaskService.class);

    private final TaskRepository repository;

    private final int userMaxTasks;

    private final int defaultPageSize;

    private final int defaultPageNum;

    public DefaultTaskService(TaskRepository repository, int userMaxTasks, int defaultPageSize, int defaultPageNum) {
        this.repository = repository;
        this.userMaxTasks = userMaxTasks;
        this.defaultPageSize = defaultPageSize;
        this.defaultPageNum = defaultPageNum;
    }

    @Override
    public TaskDto getTask(GetTask.Request request) {
        Task task = repository.getTaskById(request.taskId())
                .orElseThrow(() -> new TaskNotFoundException(request.taskId()));
        return TaskMapper.MapToDto(task);
    }

    @Override
    public List<TaskDto> getAllTasks(GetAllTasks.Request request) {
        var query = new Query(
                request.creatorId(),
                request.assignedUserId(),
                ImportanceMapper.INSTANCE.toDomain(request.importance()),
                StatusMapper.INSTANCE.toDomain(request.status()),
                request.pageSize() == null ? defaultPageSize : request.pageSize(),
                request.pageNum() == null ? defaultPageNum : request.pageNum());
        return repository
                .getAllTasks(query)
                .stream()
                .map(TaskMapper::MapToDto)
                .toList();
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
        Priority newPriority = ImportanceMapper.INSTANCE.toDomain(request.importance());
        Long taskId = request.taskId();
        Long newCreatorId = request.creatorId();
        LocalDateTime newCreateDateTime = request.createDateTime();
        LocalDateTime newDeadlineDateTime = request.deadlineDateTime();

        Long newAssignedUserId = request.assignedUserId();
        Task task = repository.getTaskById(request.taskId())
                .orElseThrow(() -> new TaskNotFoundException(request.taskId()));

        if (task.status() == Status.DONE) {
            throw new TaskOperationException("Cannot update task. Status: " + task.status());
        }

        if (!newAssignedUserId.equals(task.id()) && task.status().equals(Status.IN_PROGRESS)) {
            assertUserCanStartNewTask(newAssignedUserId, taskId);
        }

        task = new Task(
                task.id(),
                newCreatorId,
                newAssignedUserId,
                task.status(),
                newCreateDateTime,
                newDeadlineDateTime,
                newPriority,
                task.doneDateTime());
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

        assertUserCanStartNewTask(task.assignedUserId(), task.id());

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

        assertUserCanFinishTask(task.assignedUserId(), task.id());

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

    private void assertUserCanStartNewTask(Long userId, Long taskId) {
        if (userId == null) {
            throw new TaskOperationException("Cannot start task with id=" + taskId + " -- user is not assigned");
        }

        long numberOfTasks = repository.countTasksByAssignedUserIdAndStatus(
                userId, Status.IN_PROGRESS);
        if (numberOfTasks >= userMaxTasks) {
            throw new TaskOperationException("Cannot start task for user with id=" + userId + " -- already has " + userMaxTasks + " tasks");
        }
    }

    private void assertUserCanFinishTask(Long userId, Long taskId) {
        if (userId == null) {
            throw new TaskOperationException("Cannot finish task with id=" + taskId + " -- user is not assigned");
        }
    }
}
