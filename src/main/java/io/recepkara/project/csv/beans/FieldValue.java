package io.recepkara.project.csv.beans;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class FieldValue {
    private final String name;
    private final String value;
}
