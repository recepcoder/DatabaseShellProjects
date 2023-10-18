package io.recepkara.project.sql.query;

import io.recepkara.project.csv.beans.Order;
import lombok.Data;

import java.util.List;
@Data
public class SelectQuery extends TableAndFiltersQuery  {
    private List<String> fields;
    private Order order;
}
