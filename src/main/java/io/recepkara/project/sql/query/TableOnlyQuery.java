package io.recepkara.project.sql.query;

import lombok.Data;

import java.util.List;

@Data
public class TableOnlyQuery implements Query {
    private String tableName;

}
