package io.recepkara.project.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SqlKeyword {
    SELECT("SELECT"),
    FROM("FROM"),
    WHERE("WHERE"),
    ORDER_BY("ORDER BY"),
    UPDATE("UPDATE"),
    SET("SET"),
    INSERT_INTO("INSERT INTO"),
    VALUES("VALUES"),
    DELETE_FROM("DELETE FROM"),
    CREATE_TABLE("CREATE TABLE"),
    DROP_TABLE("DROP TABLE"),
    AND(" AND "),
    COMMA(","),
    LEFT_PARENTHESIS("("),
    RIGHT_PARENTHESIS(")");


    private final String sqlValue;

}
