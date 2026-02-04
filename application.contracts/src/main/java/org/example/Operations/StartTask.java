package org.example.Operations;

import org.example.Dto.TaskDto;

public class StartTask {
    public static record Request(long taskId) {}

    public static abstract class Response {
        public static final class Success extends StartTask.Response {
        }

        public static final class Failure extends StartTask.Response {

            public final String reason;

            public Failure(String reason) {
                this.reason = reason;
            }
        }
    }
}
