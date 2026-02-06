package org.example;

import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskEntity toEntity(Task task) {
        return new TaskEntity(
                task.id(),
                task.creatorId(),
                task.assignedUserId(),
                task.status(),
                task.createDateTime(),
                task.deadlineDateTime(),
                task.priority(),
                task.doneDateTime());
    }

    public Task toDomain(TaskEntity entity) {
        return new Task(
                entity.getId(),
                entity.getCreatorId(),
                entity.getAssignedUserId(),
                entity.getStatus(),
                entity.getCreateDateTime(),
                entity.getDeadlineDateTime(),
                entity.getPriority(),
                entity.getDoneDateTime()
        );
    }
}
