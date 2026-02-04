package org.example.Operations;

import org.example.Dto.TaskDto;

import java.time.LocalDateTime;

public abstract class CreateTask {
    public record Request(
            Long creatorId,
            Long assignedUserId,
            LocalDateTime createDateTime,
            LocalDateTime deadlineDateTime,
            String importance) {}

    public interface Response {
        record Success(TaskDto createdTask) implements Response  {
        }

        record Failure(String reason) implements Response {
        }
    }
}
