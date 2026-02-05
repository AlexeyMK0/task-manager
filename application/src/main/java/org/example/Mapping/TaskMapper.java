package org.example.Mapping;

import org.example.Dto.TaskDto;
import org.example.Task;

public class TaskMapper {
    public static TaskDto MapToDto(final Task task) {
        return new TaskDto(
                task.id(),
                task.creatorId(),
                task.assignedUserId(),
                task.createDateTime(),
                task.deadlineDateTime(),
                task.priority().name(),
                task.status().name(),
                task.doneDateTime());
    }
}
