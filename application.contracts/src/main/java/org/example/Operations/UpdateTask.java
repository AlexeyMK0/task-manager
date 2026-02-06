package org.example.Operations;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.example.Model.TaskImportance;
import org.example.Model.TaskStatus;

import java.time.LocalDateTime;

public abstract class UpdateTask {
    public record Request(
            long taskId,
            @NotNull
            Long creatorId,
            Long assignedUserId,
            LocalDateTime createDateTime,
            @Future
            LocalDateTime deadlineDateTime,
            @NotNull
            TaskImportance importance) {}
}
