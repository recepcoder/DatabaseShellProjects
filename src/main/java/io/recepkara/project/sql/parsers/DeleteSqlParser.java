package io.recepkara.project.sql.parsers;

import io.recepkara.project.sql.query.DeleteQuery;
import io.recepkara.project.utils.SqlKeyword;

import java.util.List;

import static io.recepkara.project.utils.SqlKeyword.*;

public class DeleteSqlParser extends AbstractSqlParser {
    private static final List<SqlKeyword> SELECT_RESERVED_WORDS = List.of(DELETE_FROM, WHERE);


    public DeleteSqlParser() {
        super(SELECT_RESERVED_WORDS);
    }

    public DeleteQuery parse(String sql) {
        setSql(sql);
        DeleteQuery deleteQuery = new DeleteQuery();

        parseTableName(deleteQuery, DELETE_FROM);
        parseFilters(WHERE, AND).ifPresent(deleteQuery::setFilters);
        return deleteQuery;
    }

}

