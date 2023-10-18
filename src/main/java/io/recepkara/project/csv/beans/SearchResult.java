package io.recepkara.project.csv.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class SearchResult {
    private final  String tableName;
    private  final List<Column> columns;
    private final List<Row> foundRows;

}
