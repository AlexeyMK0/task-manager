package org.example.Dto;

import org.example.Model.TaskImportance;
import org.example.Model.TaskStatus;

import java.time.LocalDateTime;

public record TaskDto(long id,
                      Long creatorId,
                      Long assignedUserId,
                      LocalDateTime createDateTime,
                      LocalDateTime deadlineDateTime,
                      TaskImportance importance,
                      TaskStatus status,
                      LocalDateTime doneDateTime
) {
}
