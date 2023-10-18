package io.recepkara.project.csv;

import io.recepkara.project.DatabaseEngine;
import io.recepkara.project.csv.beans.*;
import org.assertj.core.api.AbstractFileAssert;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.util.List;

import static io.recepkara.project.csv.beans.Column.*;
import static org.assertj.core.api.Assertions.*;

class CsvDatabaseEngineImplTest {
    private  String tempDir;
    private DatabaseEngine databaseEngine;
    public  static  final  String TEST_TABLE="MY_TABLE";


    @BeforeEach
    void setUp() {
        tempDir= Files.newTemporaryFolder().getAbsolutePath();
        System.out.println("TempDir: "+tempDir);
        databaseEngine=new CsvDatabaseEngineImpl(tempDir);
    }

    @AfterEach
    void tearDown() {
        Files.delete(new File(tempDir));

    }

    @Test
    void createTable() {
        createTestTable();
        assertThatFile()
                .exists()
                .hasContent("id,firstname,lastname");

    }

    @Test
    void insertTable() {
        createTestTable();
        int rows = databaseEngine.insertIntoTable(TEST_TABLE,List.of(
                Row.newRow(List.of("57","Re\"cep","Ka,ra")),
                Row.newRow(List.of("34","Ali","Kara"))
        ));
        assertThat(rows).isEqualTo(2);
        assertThatFile()
                .exists()
                .hasContent("""
    id,firstname,lastname
    "57","Re\\"cep","Ka,ra"
    "34","Ali","Kara"
    """);

    }
    @Test
    void selectTable() {
        createTestTable();
        databaseEngine.insertIntoTable
                (TEST_TABLE,
                List.of(
                        Row.newRow(List.of("57","Re\"cep","Ka,ra")),
                        Row.newRow(List.of("34","Ali","Kara")),
                        Row.newRow(List.of("01","Cihat","Kara")),
                        Row.newRow(List.of("01","James","Kara")),
                        Row.newRow(List.of("55","Eflatun","Kara"))
                        )
                );

        SearchResult searchResult=databaseEngine.selectFromTable(TEST_TABLE,List.of("firstname","lastname"),List.of(new Filter("id","01")),new Order("lastname", Order.OrderType.ASC));

        assertThat(searchResult.getColumns()).containsExactly(
                stringColumn("id"),
                stringColumn("firstname"),
                stringColumn("lastname")
        );
       // assertThat(searchResult.getFoundRows()).containsExactly(new Row(1,List.of("57","Re\"cep","Ka,ra")),new Row(2,List.of("34","Ali","Kara")));
        //filtrekontrol
       assertThat(searchResult.getFoundRows()).containsExactly(new Row(3,List.of("Cihat","Kara")),new Row(4,List.of("James","Kara")));



    }
    @Test
    void deleteTable()
    {
        createTestTable();
        databaseEngine.insertIntoTable(TEST_TABLE,List.of(
                Row.newRow(List.of("1","Recep","A")),
                Row.newRow(List.of("2","Ali","B")),
                Row.newRow(List.of("1","Adams","C")),
                Row.newRow(List.of("1","John","D"))
        ));
        int rowsDeleted=databaseEngine.deleteFromTable(TEST_TABLE,List.of(new Filter("id","1")));
        SearchResult searchResult=databaseEngine.selectFromTable(TEST_TABLE,List.of(),List.of(),null);

        assertThat(searchResult.getFoundRows()).containsExactly(
                new Row(1,List.of("2","Ali","B"))
        );

        assertThat(rowsDeleted).isEqualTo(3);
    }
    @Test
    void updateTable()
    {
        createTestTable();
        databaseEngine.insertIntoTable(TEST_TABLE,List.of(
                Row.newRow(List.of("1","Recep","A")),
                Row.newRow(List.of("2","Ali","B")),
                Row.newRow(List.of("1","Adams","C")),
                Row.newRow(List.of("1","Anna","D"))
        ));
        int rowsUpdated=databaseEngine.updateTable(TEST_TABLE,List.of(new FieldValue("lastname","Hello")),List.of(new Filter("id","1")));
        SearchResult searchResult=databaseEngine.selectFromTable(TEST_TABLE,List.of(),List.of(),null);

        assertThat(searchResult.getFoundRows()).containsExactly(
                new Row(1,List.of("1","Recep","Hello")),
                new Row(2,List.of("2","Ali","B")),
                new Row(3,List.of("1","Adams","Hello")),
                new Row(4,List.of("1","Anna","Hello"))
        );

        assertThat(rowsUpdated).isEqualTo(3);
    }
    private AbstractFileAssert<?> assertThatFile() {
        return assertThat(new File(tempDir + File.separatorChar + TEST_TABLE + ".csv"));
    }

    private void createTestTable() {
        databaseEngine.createTable(TEST_TABLE, List.of(stringColumn("id"), stringColumn("firstname"), stringColumn("lastname")));
    }
}