package org.example;

import org.example.Operations.*;

public interface TaskService {

    GetTask.Response getTask(GetTask.Request request);

    GetAllTasks.Response getAllTasks();

    CreateTask.Response createTask(CreateTask.Request request);

    UpdateTask.Response updateTask(UpdateTask.Request request);

    DeleteTask.Response deleteTask(DeleteTask.Request request);
}
