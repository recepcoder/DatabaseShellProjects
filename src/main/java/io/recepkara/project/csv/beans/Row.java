package io.recepkara.project.csv.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class Row {
    private final int id;
    private final  List<String> fields;
    public static Row newRow(List<String> fields)
    {
        return new Row(0,fields);
    }
}
