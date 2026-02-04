package org.example.Operations;

import org.example.Dto.TaskDto;

import java.util.List;

public abstract class GetAllTasks {
    public interface Response {
        record Success(List<TaskDto> tasks) implements Response {
        }

        record Failure(String reason) implements Response {
        }
    }
}
