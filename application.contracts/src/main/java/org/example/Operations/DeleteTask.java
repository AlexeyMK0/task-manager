package org.example.Operations;

import org.example.Dto.TaskDto;

import java.time.LocalDateTime;

public class DeleteTask {
    public record Request(
            long taskId) {}

    public static abstract class Response {
        public static final class Success extends Response {
        }

        public static final class Failure extends Response {
            public final String reason;

            public Failure(String reason) {
                this.reason = reason;
            }
        }
    }
}
