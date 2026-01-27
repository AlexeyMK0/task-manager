package org.example;

import org.example.Mapping.TaskMapper;
import org.example.Operations.GetAllTasks;
import org.example.Operations.GetTaskById;

import java.time.LocalDateTime;
import java.util.Map;

public class DefaultTaskService implements TaskService {

    private final Map<Long, Task> repository = Map.of(
            1L, new Task(1, 10, 100, Status.CREATED, LocalDateTime.now(), LocalDateTime.now().plusDays(5), Priority.Low),
            2L, new Task(2, 20, 200, Status.IN_PROGRESS, LocalDateTime.now(), LocalDateTime.now().plusDays(2), Priority.Medium),
            3L, new Task(3, 30, 300, Status.DONE, LocalDateTime.now(), LocalDateTime.now().plusDays(1), Priority.High)
    );

    @Override
    public GetTaskById.Response getTaskById(long id) {
        if (!repository.containsKey(id)) {
            return new GetTaskById.Response.Failure("task with id " + id + " not found");
        }
        return new GetTaskById.Response.Success(
                TaskMapper.MapToDto(repository.get(id)));
    }

    @Override
    public GetAllTasks.Response getAllTasks() {
        return new GetAllTasks.Response.Success(
                repository.values().stream().map(TaskMapper::MapToDto).toList());
    }
}
