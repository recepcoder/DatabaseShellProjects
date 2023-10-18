package io.recepkara.project.sql.parsers;

import io.recepkara.project.csv.beans.Filter;
import io.recepkara.project.exception.SqlException;
import io.recepkara.project.sql.query.TableOnlyQuery;
import io.recepkara.project.utils.SqlKeyword;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.recepkara.project.utils.StringUtils.indexOfIgnoreCase;

@Setter@Getter
@RequiredArgsConstructor
public abstract class AbstractSqlParser {
    private String sql;
    private  final List<SqlKeyword> reservedWords;
    protected   List<String> separateValueWith(String value,String separator)
    {
        String[] arr=value.split(separator);
        return Arrays.stream(arr)
                .map(String::trim)
                .map(s->s.replaceAll("'",""))
                .map(s->s.replaceAll("\n",""))
                .toList();

    }
    protected void parseTableName(TableOnlyQuery tableOnlyQuery,SqlKeyword sqlKeyword) {
        Optional<String> tableNameOpt=parseGroup(sqlKeyword);
        tableNameOpt.ifPresentOrElse(tableOnlyQuery::setTableName, () -> {
            throw new SqlException("No fields specified in  query: " + getSql());
        });}
    protected Optional<List<Filter>> parseFilters(SqlKeyword filterListSeparator,
                                                  SqlKeyword filterItemSeparator) {
        Optional<String> valueOpt=parseGroup(filterListSeparator);
       return valueOpt.map( filtersStr -> {
            return separateValueWith(filtersStr,filterItemSeparator.getSqlValue())
                    .stream()
                    .map(filterStr -> {
                        List<String> mappings= separateValueWith(filterStr,"=");

                        String valueTrimmed=mappings.get(1).replaceAll("'","");
                        return new Filter(mappings.get(0),valueTrimmed);

                    })
                    .toList();

        });
    }
    protected Optional<String> parseGroup(SqlKeyword sqlKeyword) {
        int sqlKeywordLength = sqlKeyword.getSqlValue().length();
        int indexOfGroup = indexOfIgnoreCase(getSql(), sqlKeyword.getSqlValue(), 0);
        if (indexOfGroup >= 0) {
            Optional<Integer> indexOfNextKeyOpt = reservedWords.stream()
                    .map(k -> indexOfIgnoreCase(getSql(), k.getSqlValue(), indexOfGroup))
                    .filter(index -> index > indexOfGroup)
                    .findFirst();
            return Optional.of(
                    indexOfNextKeyOpt
                            .map(indexNextKey -> getSql().substring(indexOfGroup + sqlKeywordLength, indexNextKey))
                            .orElseGet(() -> getSql().substring(indexOfGroup + sqlKeywordLength)).trim());
        } else {
            return Optional.empty();
        }
    }
}
