package org.scheduler.example.service;

import lombok.experimental.UtilityClass;
import org.scheduler.example.domain.Task;
import org.scheduler.example.domain.TaskStatus;
import org.scheduler.example.model.TaskSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

@UtilityClass
public class TaskSpecification {

    public static Specification<Task> getTaskDueDateFrom(TaskSearchCriteria criteria) {
        return getTaskDueDateFrom(criteria.getDueDateFrom())
                .and(getTaskByStatus(criteria.getStatus()));
    }

    public static Specification<Task> getTaskBetweenDate(TaskSearchCriteria criteria) {
        return getTaskBetweenDate(criteria.getDueDateFrom(), criteria.getDueDateTo())
                .and(getTaskByStatus(criteria.getStatus()));
    }

    private static Specification<Task> getTaskDueDateFrom(LocalDateTime dueDateFrom) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), dueDateFrom);
    }

    private static Specification<Task> getTaskBetweenDate(LocalDateTime dueDateFrom, LocalDateTime dueDateTo) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.between(root.get("dueDate"), dueDateFrom, dueDateTo);
    }

    private static Specification<Task> getTaskByStatus(TaskStatus status) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("status"), status);
    }

}
