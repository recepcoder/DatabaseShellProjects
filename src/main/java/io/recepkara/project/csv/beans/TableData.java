package io.recepkara.project.csv.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class TableData {
    private  final TableHeader tableHeader;
    private  final List<Row> rows;
}
