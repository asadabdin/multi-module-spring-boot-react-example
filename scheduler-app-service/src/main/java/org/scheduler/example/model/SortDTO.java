package org.scheduler.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortDTO {

    /** List of custom sorting orders */
    private List<SortOrderDTO> orders = new ArrayList<>();

}
