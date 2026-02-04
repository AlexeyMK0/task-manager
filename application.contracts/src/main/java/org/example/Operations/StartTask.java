package org.example.Operations;

public abstract class StartTask {
    public record Request(long taskId) {}

    public interface Response {
        record Success() implements Response {
        }

        record Failure(String reason) implements Response {
        }
    }
}
