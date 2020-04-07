package org.scheduler.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.scheduler.example.domain.TaskStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskSearchCriteria {

    @NotNull
    private TaskStatus status;
    @NotNull
    private LocalDateTime dueDateFrom;
    private LocalDateTime dueDateTo;

}
