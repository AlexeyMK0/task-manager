package org.example.Operations;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

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
            String importance,
            String status) {}
}
