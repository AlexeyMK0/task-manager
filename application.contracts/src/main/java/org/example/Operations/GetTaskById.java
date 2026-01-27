package org.example.Operations;

import org.example.Dto.TaskDto;

public class GetTaskById {
    public static abstract class Response {
        public static final class Success extends Response {
            public final TaskDto task;

            public Success(TaskDto task) {
                this.task = task;
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
