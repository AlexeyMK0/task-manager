package org.example.Dto;

import java.time.LocalDateTime;

// TODO: change importance and status types to enum
public record TaskDto(long id,
                      Long creatorId,
                      Long assignedUserId,
                      LocalDateTime createDateTime,
                      LocalDateTime deadlineDateTime,
                      String importance,
                      String status,
                      LocalDateTime doneDateTime
) {
}
