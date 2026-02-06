package org.example;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaTaskRepository extends JpaRepository<TaskEntity, Long> {

    long countByAssignedUserIdAndStatus(Long assignedUserId, Status status);

    @Query("""
            SELECT t FROM TaskEntity t
            WHERE (:creatorId is NULL OR t.creatorId = :creatorId)
            AND (:assignedUserId is NULL or t.assignedUserId = :assignedUserId)
            AND (:priority is NULL or t.priority = :priority)
            AND (:status is NULL or t.status = :status)""")
    List<TaskEntity> searchAllByFilter(
            @Param("creatorId") Long creatorId,
            @Param("assignedUserId") Long assignedUserId,
            @Param("priority") Priority priority,
            @Param("status") Status status,
            Pageable pageable);
}
