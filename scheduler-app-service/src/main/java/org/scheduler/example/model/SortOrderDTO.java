package org.scheduler.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortOrderDTO {
    /** Property to sort by */
    @NotBlank
    private String property;

    /** Direction of sorting */
    @NotNull
    private Direction direction = Direction.ASC;

    public enum Direction {
        ASC,
        DESC
    }
}