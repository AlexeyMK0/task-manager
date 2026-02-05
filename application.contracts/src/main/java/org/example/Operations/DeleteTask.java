package org.example.Operations;

public abstract class DeleteTask {
    public record Request(
            long taskId) {}
}
