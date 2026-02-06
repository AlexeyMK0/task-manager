package org.example.Operations;

import org.example.Model.TaskImportance;
import org.example.Model.TaskStatus;

public abstract class GetAllTasks {
    public record Request(
            Long creatorId,
            Long assignedUserId,
            TaskImportance importance,
            TaskStatus status,
            Integer pageSize,
            Integer pageNum
    ) {
    }
}
