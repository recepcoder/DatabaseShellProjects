package io.recepkara.project.sql.parsers;


import io.recepkara.project.csv.beans.Row;
import io.recepkara.project.sql.query.InsertQuery;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class InsertSqlParserTest {

    @Test
    void insertQuery()
    {
        InsertSqlParser sqlParser =new InsertSqlParser();
        InsertQuery insertQuery=sqlParser.parse("INSERT INTO CUSTOMERS VALUES (1, 'RECEP KARA', 'ISTANBUL')");
        Assertions.assertThat(insertQuery.getTableName()).isEqualTo("CUSTOMERS");
        Assertions.assertThat(insertQuery.getRows()).containsExactly(
                new Row(0, List.of("RECEP KARA","ISTANBUL"))
        );


    }

}