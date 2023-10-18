package io.recepkara.project.sql.query;

import io.recepkara.project.csv.beans.Column;
import io.recepkara.project.csv.beans.Filter;
import io.recepkara.project.csv.beans.Order;
import lombok.Data;

import java.util.List;

@Data
public class CreateQuery extends TableOnlyQuery {
    private List<Column> columns;

}
