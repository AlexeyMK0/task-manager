package org.example;

public record Query(
        Long creatorId,
        Long assignedUserId,
        Priority priority,
        Status status,
        int pageSize,
        int pageNum
) {
}
