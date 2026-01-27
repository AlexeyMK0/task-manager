package org.example;

import org.example.Dto.TaskDto;
import org.example.Operations.GetAllTasks;
import org.example.Operations.GetTaskById;

import java.util.List;

public interface TaskService {

    GetTaskById.Response getTaskById(long id);

    GetAllTasks.Response getAllTasks();
}
