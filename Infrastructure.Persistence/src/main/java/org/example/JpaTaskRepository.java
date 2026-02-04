package org.example;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaTaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByAssignedUserIdAndStatus(Long AssignedUserId, Status status);
}
