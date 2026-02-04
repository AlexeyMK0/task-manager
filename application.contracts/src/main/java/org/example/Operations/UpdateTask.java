package org.example.Operations;

import org.example.Dto.TaskDto;

import java.time.LocalDateTime;

public abstract class UpdateTask {
    public record Request(
            long taskId,
            Long creatorId,
            Long assignedUserId,
            LocalDateTime createDateTime,
            LocalDateTime deadlineDateTime,
            String importance,
            String status) {}

    public interface Response {
        record Success(TaskDto updatedTask) implements Response {
        }

        record Failure(String reason) implements Response {
        }
    }
}
