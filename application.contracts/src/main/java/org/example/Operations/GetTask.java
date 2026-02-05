package org.example.Operations;

import org.example.Dto.TaskDto;

public abstract class GetTask {
    public record Request(long taskId) {}
}
