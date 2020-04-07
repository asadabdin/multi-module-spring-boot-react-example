package org.scheduler.example.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.scheduler.example.model.PageRequestDTO;
import org.scheduler.example.model.TaskSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskSearchCriteriaValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return PageRequestDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        @SuppressWarnings("unchecked")
        PageRequestDTO<TaskSearchCriteria> request = (PageRequestDTO<TaskSearchCriteria>) o;
        Optional.ofNullable(request.getCriteria().getDueDateTo())
                .filter(toDate -> toDate.isBefore(request.getCriteria().getDueDateFrom()))
                .ifPresent(toDate -> {
                    log.debug("toDate {} is before then fromDate {}", toDate, request.getCriteria().getDueDateFrom());
                    errors.rejectValue("criteria.dueDateTo",
                            "field.invalid",
                            new Object[]{request.getCriteria().getDueDateTo()},
                            "To Date cannot be before From Date");
                });
    }
}
