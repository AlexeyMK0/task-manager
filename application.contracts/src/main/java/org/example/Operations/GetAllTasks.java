package org.example.Operations;

import org.example.Model.TaskImportance;
import org.example.Model.TaskStatus;

import java.util.List;

public abstract class GetAllTasks {
    public record Request(
            List<Long> creatorIds,
            List<Long> assignedUserIds,
            List<TaskImportance> importanceList,
            List<TaskStatus> statusList,
            Integer pageSize,
            Integer pageNum
    ) {
    }
}
