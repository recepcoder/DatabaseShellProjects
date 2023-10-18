package io.recepkara.project.shell;

import io.recepkara.project.SqlDatabaseEngine;
import io.recepkara.project.csv.CsvDatabaseEngineImpl;
import io.recepkara.project.csv.beans.Column;
import io.recepkara.project.csv.beans.Row;
import io.recepkara.project.csv.beans.SearchResult;
import io.recepkara.project.exception.SqlException;
import io.recepkara.project.sql.SqlDatabaseEngineImpl;
import io.recepkara.project.sql.parsers.GenericSqlParser;

import java.util.List;


public class ShellSqlDatabaseEngine {

    private  final SqlDatabaseEngine sqlDatabaseEngine=new SqlDatabaseEngineImpl(
            new CsvDatabaseEngineImpl("C:\\Users\\recep.kara\\Desktop\\ShellDB"),
            GenericSqlParser.newWithDefaultParsers()
    );
    public void parseSql(String sql)
    {
        String trimmedSql=sql.trim();
        String actionName=trimmedSql.substring(0,trimmedSql.indexOf(" ")).trim().toLowerCase();
        switch (actionName) {
            case "create"->handleCreate(sql);
            case "drop"  ->handleDrop(sql);
            case "insert"->handleInsert(sql);
            case "delete"->handleDelete(sql);
            case "update"->handleUpdate(sql);
            case "select"->handleSelect(sql);
            default -> throw new SqlException("Unknown sql action: " + actionName);

        }
    }
    private  void handleSelect(String sql)
    {
        try {
            SearchResult searchResult=sqlDatabaseEngine.executeSelect(sql);
            if (searchResult.getFoundRows().isEmpty()) {
                System.out.println(" No records found");
            }
            else
            {
                int nrOfColumns=searchResult.getColumns().size();
                printLine(nrOfColumns,'=');
                displayColumns(searchResult.getColumns());
                printLine(nrOfColumns,'-');
                displayRows(searchResult.getFoundRows());
                printLine(nrOfColumns,'=');
                System.out.println(searchResult.getFoundRows().size()+" row(s) found");

            }

        }
        catch (RuntimeException ex)
        {
            System.err.println("Error: " + ex.getMessage());
        }

    }
    private  void printLine(int nrOfColumns,char character)
    {
        int nrOfCharacters=(nrOfColumns*12)+1;

        for (int i=1;i<=nrOfCharacters;i++) {
            System.out.print(character);
        }
        System.out.println();
    }

    private void displayRows(List<Row> foundRows) {
        for (Row row:foundRows) {
            System.out.print("|");
            for (String value: row.getFields()) {
                System.out.printf("%-10s|",value);
            }
            System.out.printf("%n");
        }

    }

    private static void displayColumns(List<Column> columns) {
        System.out.print("|");
        for (Column c : columns) {
            System.out.printf("%-10s|",c.getName());
        }
        System.out.printf("%n");
    }

    private  void handleCreate(String sql)
    {
        try {
            sqlDatabaseEngine.execute(sql);
            System.out.println();
            System.out.println(" Table successfully created");
        }catch (RuntimeException ex)
        {
            System.err.println("Error: " + ex);
        }

    }
    private  void handleDrop(String sql)
    {
        try {
            sqlDatabaseEngine.execute(sql);
            System.out.println();
            System.out.println(" Table successfully dropped");
        }catch (RuntimeException ex)
        {
            System.err.println("Error: " + ex);
        }

    }
    private  void handleInsert(String sql)
    {
        try {
            int insertedRows=sqlDatabaseEngine.executeUpdate(sql);
            System.out.println();
            System.out.println( insertedRows + " Row(s) successfully inserted");
        }catch (RuntimeException ex)
        {
            System.err.println("Error: " + ex);
        }

    }
    private  void handleDelete(String sql)
    {
        try {
            int deletedRows=sqlDatabaseEngine.executeUpdate(sql);
            System.out.println();
            System.out.println( deletedRows + " Row(s) successfully deleted");
        }catch (RuntimeException ex)
        {
            System.err.println("Error: " + ex);
        }

    }
    private  void handleUpdate(String sql)
    {
        try {
            int updatedRows=sqlDatabaseEngine.executeUpdate(sql);
            System.out.println();
            System.out.println( updatedRows + "Row(s) successfully updated");
        }catch (RuntimeException ex)
        {
            System.err.println("Error: " + ex);
        }

    }
}
