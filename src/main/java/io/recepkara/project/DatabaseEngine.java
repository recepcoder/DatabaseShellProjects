package io.recepkara.project;

import io.recepkara.project.csv.beans.*;

import java.util.List;

public interface DatabaseEngine {
    void  createTable(String tableName, List<Column> columns);
    int insertIntoTable(String tableName, List<Row> rows);
    SearchResult selectFromTable(String tableName, List<String> fields, List<Filter> filters, Order order);
    int deleteFromTable(String tableName,List<Filter> filters);
    void dropTable(String tableName);
    int updateTable(String tableName,List<FieldValue> updatedValues,List<Filter> filters);
}
