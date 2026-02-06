package org.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JpaTaskRepository
        extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {

    long countByAssignedUserIdAndStatus(Long assignedUserId, Status status);
}
