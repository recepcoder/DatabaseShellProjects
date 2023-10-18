package io.recepkara.project.sql.query;

import io.recepkara.project.csv.beans.Column;
import io.recepkara.project.csv.beans.Row;
import lombok.Data;

import java.util.List;

@Data
public class InsertQuery extends TableOnlyQuery  {
    private List<Row> rows;

}
