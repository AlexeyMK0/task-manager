package org.example.Dto;

import java.time.LocalDateTime;

// TODO: change priority and status types to enum
public record TaskDto(long id,
                      Long creatorId,
                      Long assignedUserId,
                      LocalDateTime createDateTime,
                      LocalDateTime deadlineDateTime,
                      String priority,
                      String status
) {
}
