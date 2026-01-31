package org.example;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> getAllTasks();

    Optional<Task> getTaskById(long id);

    Task create(Task taskToCreate);

    Task update(Task taskToUpdate);

    void delete(Task taskToDelete);
}
