package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostgresTaskRepository implements TaskRepository {

    private final JpaTaskRepository taskRepository;

    private final TaskMapper mapper;

    @Autowired
    public PostgresTaskRepository(JpaTaskRepository taskRepository, TaskMapper mapper) {
        this.taskRepository = taskRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public long countTasksByAssignedUserIdAndStatus(Long assignedUserId, Status status) {
        return taskRepository.countByAssignedUserIdAndStatus(assignedUserId, status);
    }

    @Override
    public Optional<Task> getTaskById(long id) {
        return taskRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Task create(Task taskToCreate) {
        var entityToCreate = mapper.toEntity(taskToCreate);
        entityToCreate.setId(null);

        TaskEntity savedEntity = taskRepository.save(entityToCreate);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Task update(Task taskToUpdate) {
        var entityToUpdate = mapper.toEntity(taskToUpdate);

        TaskEntity updatedEntity = taskRepository.save(entityToUpdate);
        return mapper.toDomain(updatedEntity);
    }

    @Override
    public void delete(Task taskToDelete) {
        var entityToDelete = mapper.toEntity(taskToDelete);
        taskRepository.delete(entityToDelete);
    }
}
