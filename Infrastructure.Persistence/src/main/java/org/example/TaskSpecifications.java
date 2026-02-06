package org.example;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class TaskSpecifications {
    public static Specification<TaskEntity> withQuery(Query query) {
        return (from, criteriaQuery, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            addPredicateIfPresent(query.creatorIds(), "creatorId", predicates, from);
            addPredicateIfPresent(query.assignedUserIds(), "assignedUserId", predicates, from);
            addPredicateIfPresent(query.priorities(), "priority", predicates, from);
            addPredicateIfPresent(query.statuses(), "status", predicates, from);

            return builder.and(predicates);
        };
    }

    private static <T> boolean isNullOrEmpty (Collection<T> list) {
        return list == null || list.isEmpty();
    }

    private static <T> boolean addPredicateIfPresent(
            List<T> list,
            String parameterName,
            List<Predicate> whereToAdd,
            Root<TaskEntity> root) {
        if (!isNullOrEmpty(list)) {
            whereToAdd.add(root.get(parameterName).in(list));
            return true;
        }
        return false;
    }
}
