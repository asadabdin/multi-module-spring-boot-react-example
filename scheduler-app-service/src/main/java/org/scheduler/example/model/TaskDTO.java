package org.scheduler.example.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.scheduler.example.domain.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@ApiModel("Task Info")
public class TaskDTO {

    @ApiModelProperty("Unique identifier of Task")
    private UUID id;

    @ApiModelProperty("Due Date of the Task")
    private LocalDateTime dueDate;

    @ApiModelProperty("The date on which the Task has resolved")
    private LocalDateTime resolvedAt;

    @ApiModelProperty("Title of the Task")
    private String title;

    @ApiModelProperty("Description of the Task")
    private String description;

    @ApiModelProperty("Priority of the task")
    private Integer priority;

    @ApiModelProperty("Status of the task")
    private TaskStatus status;

    @ApiModelProperty("Th date on which this task has created")
    private LocalDateTime createdAt;

    @ApiModelProperty("The date on which the task has updated")
    private LocalDateTime updatedAt;

}
