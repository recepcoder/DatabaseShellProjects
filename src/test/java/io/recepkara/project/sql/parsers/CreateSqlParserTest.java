package io.recepkara.project.sql.parsers;

import io.recepkara.project.csv.beans.Column;
import io.recepkara.project.sql.query.CreateQuery;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CreateSqlParserTest {

    @Test
    void createQuery()
    {
        CreateSqlParser sqlParser= new CreateSqlParser();
       CreateQuery createQuery = sqlParser.parse("""
                CREATE TABLE CUSTOMERS (
                    ID STRING,
                    NAME STRING,
                    PLACE STRING
                )
                """);

       assertThat(createQuery.getTableName()).isEqualTo("CUSTOMERS");
       assertThat(createQuery.getColumns()).containsExactly(
               new Column("ID","STRING"),
               new Column("NAME","STRING"),
               new Column("PLACE","STRING")
       );
    }

}