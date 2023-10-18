package io.recepkara.project.sql.parsers;

import io.recepkara.project.exception.SqlException;
import io.recepkara.project.sql.query.Query;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GenericSqlParser {

    private final  CreateSqlParser createSqlParser;
    private final  DeleteSqlParser deleteSqlParser;
    private final  InsertSqlParser insertSqlParser;
    private final  SelectSqlParser selectSqlParser;
    private final  DropSqlParser dropSqlParser;
    private final  UpdateSqlParser updateSqlParser;

    public  static GenericSqlParser newWithDefaultParsers()
    {
        return new GenericSqlParser(
                new CreateSqlParser(),
                new DeleteSqlParser(),
                new InsertSqlParser(),
                new SelectSqlParser(),
                new DropSqlParser(),
                new UpdateSqlParser()
        );
    }
    public Query parseSql(String sql)
    {
        String trimmedSql=sql.trim();
        String actionName=trimmedSql.substring(0,trimmedSql.indexOf(" ")).trim().toLowerCase();
       return switch (actionName){
            case "create" -> new CreateSqlParser().parse(sql);
            case "delete" -> new DeleteSqlParser().parse(sql);
            case "insert" -> new InsertSqlParser().parse(sql);
            case "select" -> new SelectSqlParser().parse(sql);
            case "drop" -> new DropSqlParser().parse(sql);
            case "update" -> new UpdateSqlParser().parse(sql);
            default -> throw new SqlException("Unknown sql action: "+ actionName);

        };
    }
}
