package io.recepkara.project.sql.query;

import io.recepkara.project.csv.beans.FieldValue;
import io.recepkara.project.csv.beans.Filter;
import io.recepkara.project.csv.beans.Order;
import lombok.Data;

import java.util.List;

@Data
public class UpdateQuery extends TableAndFiltersQuery {
    private List<FieldValue> updatedValues;
}
