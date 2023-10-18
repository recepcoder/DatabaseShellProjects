package io.recepkara.project.sql.parsers;

import io.recepkara.project.csv.beans.FieldValue;
import io.recepkara.project.csv.beans.Filter;
import io.recepkara.project.sql.query.DeleteQuery;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteSqlParserTest {
    @Test
    void deleteQuery()
    {
        DeleteSqlParser sqlParser =new DeleteSqlParser();
        DeleteQuery deleteQuery=sqlParser.parse("DELETE FROM CUSTOMERS WHERE ID = 2 AND NAME = 'RECEP KARA' ");
        Assertions.assertThat(deleteQuery.getTableName()).isEqualTo("CUSTOMERS");

        Assertions.assertThat(deleteQuery.getFilters()).containsExactly(
                new Filter("ID","2"),
                new Filter("NAME","RECEP KARA")
        );

    }

}