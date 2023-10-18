package io.recepkara.project.sql.parsers;

import io.recepkara.project.csv.beans.Filter;
import io.recepkara.project.sql.query.DropQuery;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DropSqlParserTest {

    @Test
    void dropQuery()
    {
        DropSqlParser sqlParser =new DropSqlParser();
        DropQuery dropQuery=sqlParser.parse("DROP TABLE CUSTOMERS");
        Assertions.assertThat(dropQuery.getTableName()).isEqualTo("CUSTOMERS");


    }

}