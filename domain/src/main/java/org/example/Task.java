package org.example;

import java.time.LocalDateTime;

public record Task(
        Long id,
        Long creatorId,
        Long assignedUserId,
        Status status,
        LocalDateTime createDateTime,
        LocalDateTime deadlineDateTime,
        Priority priority,
        LocalDateTime doneDateTime
        ) {
}
