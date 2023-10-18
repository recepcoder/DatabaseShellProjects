package io.recepkara.project.sql.parsers;

import io.recepkara.project.csv.beans.FieldValue;
import io.recepkara.project.csv.beans.Filter;
import io.recepkara.project.sql.query.UpdateQuery;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateSqlParserTest {
    @Test
    void updateQuery()
    {
        UpdateSqlParser sqlParser =new UpdateSqlParser();
        UpdateQuery updateQuery=sqlParser.parse("UPDATE CUSTOMERS SET NAME = 'RECEP KARA', ID = 2 WHERE ID = 1;");
        Assertions.assertThat(updateQuery.getTableName()).isEqualTo("CUSTOMERS");
        Assertions.assertThat(updateQuery.getUpdatedValues()).containsExactly(
                new FieldValue("NAME","RECEP KARA")
        );
        Assertions.assertThat(updateQuery.getUpdatedValues()).containsExactly(
                new FieldValue("NAME","RECEP KARA"),
                new FieldValue("ID","2")
        );
        Assertions.assertThat(updateQuery.getFilters()).containsExactly(
                new Filter("ID","1")
        );

    }

}