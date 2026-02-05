package org.example;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostgresTaskRepository implements TaskRepository {

    private final JpaTaskRepository taskRepository;

    public PostgresTaskRepository(JpaTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll().stream().map(this::mapToTask).toList();
    }

    @Override
    public long countTasksByAssignedUserIdAndStatus(Long assignedUserId, Status status) {
        return taskRepository.countByAssignedUserIdAndStatus(assignedUserId, status);
    }

    @Override
    public Optional<Task> getTaskById(long id) {
        return taskRepository.findById(id).map(this::mapToTask);
    }

    @Override
    public Task create(Task taskToCreate) {
        var entityToCreate = mapToEntity(taskToCreate);
        entityToCreate.setId(null);

        TaskEntity savedEntity = taskRepository.save(entityToCreate);
        return mapToTask(savedEntity);
    }

    @Override
    public Task update(Task taskToUpdate) {
        var entityToUpdate = mapToEntity(taskToUpdate);

        TaskEntity updatedEntity = taskRepository.save(entityToUpdate);
        return mapToTask(updatedEntity);
    }

    @Override
    public void delete(Task taskToDelete) {
        var entityToDelete = mapToEntity(taskToDelete);

        taskRepository.delete(entityToDelete);
    }

    private Task mapToTask(TaskEntity entity) {
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

    private TaskEntity mapToEntity(Task task) {
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
}
