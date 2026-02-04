package org.example.Operations;

import org.example.Dto.TaskDto;

import java.time.LocalDateTime;

public class CreateTask {
    public record Request(
            Long creatorId,
            Long assignedUserId,
            LocalDateTime createDateTime,
            LocalDateTime deadlineDateTime,
            String importance) {}

    public static abstract class Response {
        public static final class Success extends Response {
            public final TaskDto createdTask;

            public Success(TaskDto createdTask) {
                this.createdTask = createdTask;
            }
        }

        public static final class Failure extends Response {
            public final String reason;

            public Failure(String reason) {
                this.reason = reason;
            }
        }
    }
}
