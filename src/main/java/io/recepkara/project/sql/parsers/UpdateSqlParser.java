package io.recepkara.project.sql.parsers;

import io.recepkara.project.csv.beans.FieldValue;
import io.recepkara.project.sql.query.UpdateQuery;
import io.recepkara.project.utils.SqlKeyword;

import java.util.List;
import static io.recepkara.project.utils.SqlKeyword.*;

public class UpdateSqlParser extends AbstractSqlParser {
    private static final List<SqlKeyword> SELECT_RESERVED_WORDS = List.of(UPDATE, SET, WHERE);


    public UpdateSqlParser() {
        super(SELECT_RESERVED_WORDS);
    }

    public UpdateQuery parse(String sql) {
        setSql(sql);
        UpdateQuery updateQuery = new UpdateQuery();

        parseTableName(updateQuery, UPDATE);
        parseFilters(WHERE, COMMA).ifPresent(updateQuery::setFilters);
        parseMapping(updateQuery);
        return updateQuery;
    }
    private void parseMapping(UpdateQuery updateQuery)
    {
        parseFilters(SET, COMMA).ifPresent(filters -> {
            updateQuery.setUpdatedValues(
                    filters.stream()
                            .map(filter -> new FieldValue(filter.getName(),filter.getValue()))
                            .toList());
        });
    }
}

