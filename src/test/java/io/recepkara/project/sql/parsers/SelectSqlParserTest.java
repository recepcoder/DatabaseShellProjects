package io.recepkara.project.sql.parsers;
import io.recepkara.project.csv.beans.Filter;
import io.recepkara.project.csv.beans.Order;
import io.recepkara.project.sql.query.SelectQuery;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class SelectSqlParserTest {

    @Test
    void selectFromTable()
    {
        SelectSqlParser parser=new SelectSqlParser();
        SelectQuery query;
/*        query = parser.parse("select *       from table");
        assertThat(query.getFields()).containsExactly("*");
        System.out.println(query);*/
        query = parser.parse("SELECT ID, NAME, PLACE FROM CUSTOMERS WHERE ID = 1 AND NAME = 'RECEP KARA' ORDER BY ID DESC");
        assertThat(query.getTableName()).isEqualTo("CUSTOMERS");
        assertThat(query.getFields()).containsExactly("ID","NAME","PLACE");
        assertThat(query.getFilters()).containsExactly(new Filter("ID","1"),new Filter("NAME","'RECEP KARA'"));
        //assertThat(query.getTableName()).isEqualTo("table1");
        System.out.println(query);
        assertThat(query.getOrder()).isEqualTo(new Order("ID", Order.OrderType.DESC));


    }

}