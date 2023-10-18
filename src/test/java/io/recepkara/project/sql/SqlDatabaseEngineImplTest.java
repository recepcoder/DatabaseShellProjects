package io.recepkara.project.sql;

import io.recepkara.project.DatabaseEngine;
import io.recepkara.project.SqlDatabaseEngine;
import io.recepkara.project.csv.CsvDatabaseEngineImpl;
import io.recepkara.project.csv.beans.Row;
import io.recepkara.project.csv.beans.SearchResult;
import io.recepkara.project.exception.DatabaseException;
import io.recepkara.project.sql.parsers.GenericSqlParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.util.Files.temporaryFolderPath;
import static org.junit.jupiter.api.Assertions.*;

class SqlDatabaseEngineImplTest {

    private  String basePath= temporaryFolderPath();
    private DatabaseEngine databaseEngine=new CsvDatabaseEngineImpl(basePath);
    private SqlDatabaseEngine sqlDatabaseEngine=new SqlDatabaseEngineImpl(databaseEngine, GenericSqlParser.newWithDefaultParsers());

    @Test
    void testAll()
    {
        sqlDatabaseEngine.execute("""
               DROP TABLE CUSTOMERS
                """);
        sqlDatabaseEngine.execute("""
                CREATE TABLE CUSTOMERS (
                    ID STRING,
                    NAME STRING,
                    PLACE STRING
                )
                """);
       sqlDatabaseEngine.executeUpdate("""
                INSERT INTO CUSTOMERS VALUES (1, 'RECEP KARA', 'ISTANBUL')
                """);
        sqlDatabaseEngine.executeUpdate("""
                INSERT INTO CUSTOMERS VALUES (2, 'Gabriel KARA', 'ISTANBUL')
                """);
        sqlDatabaseEngine.executeUpdate("""
                INSERT INTO CUSTOMERS VALUES (3, 'Joy KARA', 'ISTANBUL')
                """);
       SearchResult searchResult = sqlDatabaseEngine.executeSelect("""
                SELECT ID, NAME, PLACE FROM CUSTOMERS
                                
                """);
       Assertions.assertThat(searchResult.getFoundRows()).containsExactly(
               new Row(1,List.of("1","RECEP KARA","ISTANBUL")),
               new Row(2,List.of("2","Gabriel KARA","ISTANBUL")),
               new Row(3,List.of("3","Joy KARA","ISTANBUL"))
       );
        sqlDatabaseEngine.executeUpdate("""
                INSERT INTO CUSTOMERS VALUES (4, 'John KARA', 'ISTANBUL')
                """);
        searchResult = sqlDatabaseEngine.executeSelect("""
                SELECT * FROM CUSTOMERS
                                
                """);
        Assertions.assertThat(searchResult.getFoundRows()).containsExactly(
                new Row(1,List.of("1","RECEP KARA","ISTANBUL")),
                new Row(2,List.of("2","Gabriel KARA","ISTANBUL")),
                new Row(3,List.of("3","Joy KARA","ISTANBUL")),
                new Row(4,List.of("4","John KARA","ISTANBUL"))
        );

        sqlDatabaseEngine.executeUpdate("""
                DELETE FROM CUSTOMERS WHERE ID=1
                """);

        searchResult = sqlDatabaseEngine.executeSelect("""
                SELECT * FROM CUSTOMERS
                                
                """);
        Assertions.assertThat(searchResult.getFoundRows()).containsExactly(

                new Row(1,List.of("2","Gabriel KARA","ISTANBUL")),
                new Row(2,List.of("3","Joy KARA","ISTANBUL")),
                new Row(3,List.of("4","John KARA","ISTANBUL"))
        );
        sqlDatabaseEngine.execute("""
               DROP TABLE CUSTOMERS
                """);

        Assertions.assertThatThrownBy(()->{
            sqlDatabaseEngine.executeSelect("""
                SELECT * FROM CUSTOMERS
                                
                """);
        }).isInstanceOf(DatabaseException.class).hasMessage("Table: CUSTOMERS already does not exists");

    }

}