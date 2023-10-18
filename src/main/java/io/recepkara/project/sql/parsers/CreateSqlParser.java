package io.recepkara.project.sql.parsers;

import io.recepkara.project.csv.beans.Column;
import io.recepkara.project.exception.SqlException;
import io.recepkara.project.sql.query.CreateQuery;

import java.util.List;
import java.util.Optional;

import static io.recepkara.project.csv.CsvDatabaseEngineImpl.COMMA_CHAR;
import static io.recepkara.project.utils.SqlKeyword.*;

public class CreateSqlParser extends AbstractSqlParser {
    public CreateSqlParser() {
        super(List.of(CREATE_TABLE,LEFT_PARENTHESIS,RIGHT_PARENTHESIS));
    }

    public CreateQuery parse(String sql) {
        setSql(sql);
        CreateQuery createQuery = new CreateQuery();

        parseTableName(createQuery,CREATE_TABLE);
        parseColumns(createQuery);
//        parseFields(createQuery);
//        parseWhere(createQuery);
//        parseOrderby(createQuery);
        return createQuery;
    }

    private void parseColumns(CreateQuery createQuery) {
        Optional<String> valueOpt = parseGroup(LEFT_PARENTHESIS);
        valueOpt.ifPresent(s->{
            if (s.trim().length()==0) {
                throw new SqlException("No columns defined for create query");
            }
        });
        valueOpt.ifPresentOrElse(
                columnsStr ->{
                        List<String> strings=separateValueWith(columnsStr, COMMA_CHAR);
                       List<Column> columns= strings.stream().map( string ->{
                           List<String> columnList = separateValueWith(string," ");
                           return  new Column(columnList.get(0),columnList.get(1));
                        }).toList();
                        createQuery.setColumns(columns);
                },()->{
                    throw new SqlException("No columns provided for create query: " + getSql());
                }
        );
    }
}
