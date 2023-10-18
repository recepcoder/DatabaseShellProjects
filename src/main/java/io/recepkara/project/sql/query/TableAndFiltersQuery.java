package io.recepkara.project.sql.query;

import io.recepkara.project.csv.beans.Filter;
import lombok.Data;

import java.util.List;

@Data
public class TableAndFiltersQuery extends  TableOnlyQuery{
    private List<Filter> filters;

}
