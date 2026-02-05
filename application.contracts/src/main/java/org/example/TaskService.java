package org.example;

import org.example.Dto.TaskDto;
import org.example.Operations.*;

import java.util.List;

public interface TaskService {

    TaskDto getTask(GetTask.Request request);

    List<TaskDto> getAllTasks();

    TaskDto createTask(CreateTask.Request request);

    TaskDto updateTask(UpdateTask.Request request);

    void deleteTask(DeleteTask.Request request);

    void startTask(StartTask.Request request);
}
