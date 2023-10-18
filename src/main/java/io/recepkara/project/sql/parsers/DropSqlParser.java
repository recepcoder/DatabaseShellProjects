package io.recepkara.project.sql.parsers;

import io.recepkara.project.sql.query.DropQuery;
import io.recepkara.project.utils.SqlKeyword;

import java.util.List;

import static io.recepkara.project.utils.SqlKeyword.*;

public class DropSqlParser extends AbstractSqlParser {
    private static final List<SqlKeyword> SELECT_RESERVED_WORDS = List.of(DROP_TABLE);


    public DropSqlParser() {
        super(SELECT_RESERVED_WORDS);
    }

    public DropQuery parse(String sql) {
        setSql(sql);
        DropQuery dropQuery = new DropQuery();

        parseTableName(dropQuery,DROP_TABLE);
        return dropQuery;
    }

}

