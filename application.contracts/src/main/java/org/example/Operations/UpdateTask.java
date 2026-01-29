package org.example.Operations;

import org.example.Dto.TaskDto;

import java.time.LocalDateTime;

public class UpdateTask {
    public record Request(
            long taskId,
            long creatorId,
            long assignedUserId,
            LocalDateTime createDateTime,
            LocalDateTime deadlineDateTime,
            String importance,
            String status) {}

    public static abstract class Response {
        public static final class Success extends Response {
            public final TaskDto updatedTask;

            public Success(TaskDto updatedTask) {
                this.updatedTask = updatedTask;
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
