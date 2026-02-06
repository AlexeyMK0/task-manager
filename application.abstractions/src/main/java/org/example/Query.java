package org.example;

import java.util.List;

public record Query(
        List<Long> creatorIds,
        List<Long> assignedUserIds,
        List<Priority> priorities,
        List<Status> statuses,
        int pageSize,
        int pageNum
) {
}
