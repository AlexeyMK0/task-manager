package org.example.Operations;

import org.example.Dto.TaskDto;

public abstract class GetTask {
    public record Request(long taskId) {}

    public interface Response {
        record Success(TaskDto task) implements Response {
        }

        record Failure(String reason) implements Response {
        }
    }
}
