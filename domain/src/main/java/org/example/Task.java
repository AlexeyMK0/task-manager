package org.example;

import java.time.LocalDateTime;

// TODO: replace long with Long (reference type)
public record Task(
        Long id,
        long creatorId,
        long assignedUserId,
        Status status,
        LocalDateTime createDateTime,
        LocalDateTime deadlineDateTime,
        Priority priority) {
}
