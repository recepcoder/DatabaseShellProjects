package io.recepkara.project.sql.parsers;

import io.recepkara.project.csv.beans.Row;
import io.recepkara.project.exception.SqlException;
import io.recepkara.project.sql.query.InsertQuery;
import io.recepkara.project.utils.SqlKeyword;

import java.util.List;
import java.util.Optional;

import static io.recepkara.project.utils.SqlKeyword.*;

public class InsertSqlParser extends AbstractSqlParser {
    private static final List<SqlKeyword> SELECT_RESERVED_WORDS = List.of(INSERT_INTO,VALUES,LEFT_PARENTHESIS,RIGHT_PARENTHESIS);


    public InsertSqlParser() {
        super(SELECT_RESERVED_WORDS);
    }

    public InsertQuery parse(String sql) {
        setSql(sql);
        InsertQuery insertQuery = new InsertQuery();

        parseTableName(insertQuery,INSERT_INTO);
        parseRows(insertQuery);
        return insertQuery;
    }
    private void parseRows(InsertQuery insertQuery)
    {
        Optional<String> valuesOpt=parseGroup(LEFT_PARENTHESIS);

        valuesOpt.ifPresentOrElse(filters -> {
            List<String> values=separateValueWith(filters,COMMA.getSqlValue());
            insertQuery.setRows(List.of(Row.newRow(values)));
        },()-> {throw new SqlException("No records specified in query");
        });
    }
}

