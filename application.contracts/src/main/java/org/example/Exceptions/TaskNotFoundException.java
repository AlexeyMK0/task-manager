package org.example.Exceptions;

public class TaskNotFoundException extends AppException {

    private final Long taskId;

    public TaskNotFoundException(Long taskId) {
        super("Task with id=" + taskId + " not found");
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }
}
