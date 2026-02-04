package org.example;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> getAllTasks();

    List<Task> getAllTasksByAssignedUserIdAndStatus(Long assignedUserId, Status status);

    Optional<Task> getTaskById(long id);

    Task create(Task taskToCreate);

    Task update(Task taskToUpdate);

    void delete(Task taskToDelete);
}
