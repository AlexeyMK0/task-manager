package org.example.Operations;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.example.Dto.TaskDto;

import java.time.LocalDateTime;

public abstract class CreateTask {
    public record Request(
            @NotNull
            Long creatorId,
            Long assignedUserId,
            LocalDateTime createDateTime,
            @Future
            LocalDateTime deadlineDateTime,
            @NotNull
            String importance) {}
}
