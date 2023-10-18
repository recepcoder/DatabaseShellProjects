package io.recepkara.project.csv.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Filter {
    private final String name;
    private final String value;
}
