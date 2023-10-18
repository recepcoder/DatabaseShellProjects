package io.recepkara.project.sql.parsers;
import io.recepkara.project.exception.SqlException;
import io.recepkara.project.sql.query.SelectQuery;
import io.recepkara.project.utils.SqlKeyword;
import io.recepkara.project.csv.beans.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.recepkara.project.csv.CsvDatabaseEngineImpl.COMMA_CHAR;
import static io.recepkara.project.utils.SqlKeyword.*;

public class SelectSqlParser extends AbstractSqlParser {
    private static final List<SqlKeyword> SELECT_RESERVED_WORDS = List.of(SELECT, FROM, WHERE, ORDER_BY);


    public SelectSqlParser() {
        super(SELECT_RESERVED_WORDS);
    }

    public SelectQuery parse(String sql) {
        setSql(sql);
        SelectQuery selectQuery = new SelectQuery();

        parseFields(selectQuery);
        parseTableName(selectQuery,FROM);
        parseFilters(WHERE,AND).ifPresent(selectQuery::setFilters);
        parseOrderby(selectQuery);
        return selectQuery;
    }

    private void parseOrderby(SelectQuery selectQuery) {
        Optional<String> valueOpt = parseGroup(ORDER_BY);
        valueOpt.ifPresent(
                orderByStr ->{
                    if (orderByStr.contains(" ")) {
                        List<String> strings = separateValueWith(orderByStr," ");
                        selectQuery.setOrder(new Order(strings.get(0),Order.OrderType.valueOf(strings.get(1).toUpperCase())));
                    }
                    else {
                       selectQuery.setOrder( new Order(orderByStr,Order.OrderType.ASC));
                    }
                }
        );
    }

    private void parseFields(SelectQuery selectQuery) {
        //int indexOfSelect = sql.indexOf("SELECT");
        Optional<String> fieldsOpt = parseGroup(SELECT);

        fieldsOpt.ifPresentOrElse(fieldsStr ->
                {
                    String[] strings = fieldsStr.split(COMMA_CHAR);
                    List<String> fields = Arrays.stream(strings).map(String::trim).toList();
                    selectQuery.setFields(fields);
                },
                () ->
                {
                    throw new SqlException("No fields specified in select query: " + getSql());
                });
    }
}

