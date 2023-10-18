package io.recepkara.project.sql;

import io.recepkara.project.DatabaseEngine;
import io.recepkara.project.SqlDatabaseEngine;
import io.recepkara.project.csv.beans.SearchResult;
import io.recepkara.project.exception.SqlException;
import io.recepkara.project.sql.parsers.GenericSqlParser;
import io.recepkara.project.sql.query.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SqlDatabaseEngineImpl implements SqlDatabaseEngine {

    private  final DatabaseEngine databaseEngine;
    private  final GenericSqlParser genericSqlParser;
    @Override
    public SearchResult executeSelect(String sql) {
        Query query = genericSqlParser.parseSql(sql);
        if (query instanceof SelectQuery selectQuery){
            return databaseEngine.selectFromTable(
                    selectQuery.getTableName(),
                    selectQuery.getFields(),
                    selectQuery.getFilters(),
                    selectQuery.getOrder()
            );
        }else {
            throw new SqlException("Invalid select sql: "+ sql);
        }
    }

    @Override
    public void execute(String sql) {
        Query query= genericSqlParser.parseSql(sql);
        switch (query)
        {
            case CreateQuery createQuery-> databaseEngine.createTable(createQuery.getTableName(), createQuery.getColumns());
            case DropQuery dropQuery->databaseEngine.dropTable(dropQuery.getTableName());
            default -> throw new SqlException("Only create and drop are allowed here");
        }

    }

    @Override
    public int executeUpdate(String sql) {
        Query query= genericSqlParser.parseSql(sql);
    return    switch (query)
        {
            case UpdateQuery updateQuery->
                    databaseEngine.updateTable(updateQuery.getTableName(), updateQuery.getUpdatedValues(),updateQuery.getFilters());
            case InsertQuery insertQuery->
                    databaseEngine.insertIntoTable(insertQuery.getTableName(),insertQuery.getRows());
            case DeleteQuery deleteQuery->
                    databaseEngine.deleteFromTable(deleteQuery.getTableName(), deleteQuery.getFilters());

            default -> throw new SqlException("Only insert, delete and update  are allowed here");
        };

    }
}
