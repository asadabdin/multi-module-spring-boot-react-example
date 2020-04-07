package org.scheduler.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDTO<T> {

    @NotNull
    private T criteria;

    @Min(0)
    @NotNull
    private Integer page;

    @Min(1)
    @NotNull
    private Integer pageSize;

    private SortDTO sort;

}
