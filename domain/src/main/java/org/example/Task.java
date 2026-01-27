package org.example;

import java.time.LocalDateTime;

public record Task(
        long id,
        long creatorId,
        long assignedUserId,
        Status status,
        LocalDateTime createDateTime,
        LocalDateTime deadlineDateTime,
        Priority priority) {
}
