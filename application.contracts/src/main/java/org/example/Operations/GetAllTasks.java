package org.example.Operations;

import org.example.Dto.TaskDto;

import java.util.List;

public class GetAllTasks {
    public static abstract class Response {
        public static final class Success extends Response {
            public final List<TaskDto> tasks;

            public Success(List<TaskDto> tasks) {
                this.tasks = tasks;
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
