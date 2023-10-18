package io.recepkara.project.csv;

import io.recepkara.project.DatabaseEngine;
import io.recepkara.project.csv.beans.*;
import io.recepkara.project.exception.DatabaseException;
import lombok.AllArgsConstructor;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.recepkara.project.utils.ListUtils.indexOfIgnoreCase;

@AllArgsConstructor
public class CsvDatabaseEngineImpl implements DatabaseEngine {

    private final  String basePath;
    public static  final  String COMMA_CHAR =",";


    @Override
    public void createTable(String tableName, List<Column> columns) {
        verifyTableNameIsNotEmpty(tableName);
        verifyTableDoesNotExists(tableName);
        performActionInTable(tableName,true,bufferedWriter -> {
            String headerLine=columns.stream().map(column -> column.getName().trim()).collect(Collectors.joining(COMMA_CHAR));
            writeContent(bufferedWriter,headerLine,tableName);
        });
    }

    private void verifyTableNameIsNotEmpty(String tableName) {
        if (tableName==null||tableName.trim().length()==0)
        {
            throw new DatabaseException("Table name may not be empty or null");
        }
    }

    @Override
    public int insertIntoTable(String tableName, List<Row> rows)
    {
        verifyTableExist(tableName);
        return performActionInTable(tableName,true,bufferedWriter ->
        {
            rows.stream()
                .map(Row::getFields)
                .map(this::escapeCharacters)
                .forEach(line->{
                    writeContent(bufferedWriter,line,tableName);
                });
            return  rows.size();
        });

    }



    @Override
    public SearchResult selectFromTable(String tableName, List<String> fields, List<Filter> filters, Order order) {
        TableData tableData = readTableData(tableName);

        List<Row> rows=tableData.getRows();

        int columnIndex = findColumnIndexForOrderBy(fields, order, tableData);

        List<Row> foundRows= rows.stream()
                .filter(row -> applyFilters(row,filters,tableData.getTableHeader()))
                .map(row -> selectFields(row,fields,tableData.getTableHeader()))
                .sorted((row1, row2) ->sortRows(row1,row2,order,columnIndex))
                .toList();
        List<Column> columns;
        if (fields.isEmpty() || fields.get(0).trim().equals("*")) {
            columns = tableData.getTableHeader().getColumns();
        } else {
            columns=fields.stream().map(Column::stringColumn).toList();
        }
        return new SearchResult(tableName,columns,foundRows);
    }

    @Override
    public int deleteFromTable(String tableName, List<Filter> filters) {
        TableData tableData = readTableData(tableName);

        List<Row> rows=tableData.getRows();

        List<Integer> rowsToBeDeleted= rows.stream()
                .filter(row -> applyFilters(row,filters,tableData.getTableHeader()))
                .map(Row::getId)
                .sorted(Comparator.reverseOrder())
                .toList();
        rowsToBeDeleted.forEach(rowId -> tableData.getRows().remove(rowId-1));
        dropTable(tableName);
        createTable(tableName,tableData.getTableHeader().getColumns());
        insertIntoTable(tableName,tableData.getRows());
        return  rowsToBeDeleted.size();
    }

    @Override
    public void dropTable(String tableName) {
        verifyTableNameIsNotEmpty(tableName);
        File file = new File(fileNameForTable(tableName));
        if (file.exists()) {
            boolean deleted =file.delete();
            if (!deleted) {
                throw new DatabaseException("Unexpected error when dropping table: " + tableName);
            }
        }

    }

    @Override
    public int updateTable(String tableName, List<FieldValue> updatedValues, List<Filter> filters) {
        verifyTableNameIsNotEmpty(tableName);
        if (updatedValues.isEmpty()) {
            return 0;
        }
        TableData tableData = readTableData(tableName);
        List<Row> rows=tableData.getRows();
        List<Row> rowsToBeUpdated= rows.stream()
                .filter(row -> applyFilters(row,filters,tableData.getTableHeader()))
                .peek(row ->  updatedValues.forEach(fieldValue -> {
                    int indexColumn = tableData.getTableHeader().indexOfColumnWithName(fieldValue.getName());
                    row.getFields().set(indexColumn, fieldValue.getValue());
                }))
                .toList();
        if (!rowsToBeUpdated.isEmpty()) {
            dropTable(tableName);
            createTable(tableName,tableData.getTableHeader().getColumns());
            insertIntoTable(tableName,tableData.getRows());
        }
        return  rowsToBeUpdated.size();
    }
    private   String fileNameForTable(String tableName)    {
        return basePath+ File.separatorChar+tableName+".csv";
    }
    private boolean tableExists(String tableName)
    {
        String tableFile=fileNameForTable(tableName);
        return new File(tableFile).exists();
    }
    private void verifyTableExist(String tableName)    {
        if (!tableExists(tableName))
        {
            throw  new DatabaseException("Table: "+tableName+" already does not exists");
        }
    }

    private void verifyTableDoesNotExists(String tableName)    {
        if (tableExists(tableName))
        {
            throw  new DatabaseException("Table: "+tableName+" already  exists");
        }
    }
    private String escapeCharacters(List<String> values) {
        return values.stream()
                .map(CsvDatabaseEngineImpl::escapeDoubleQuotes)
                .collect(Collectors.joining(COMMA_CHAR));
    }

    private static String escapeDoubleQuotes(String s) {
        String returnValue;
        if (s.contains("\"")) {
            returnValue = s.replaceAll("\"", "\\\\\"");
        } else {
            returnValue = s;
        }
        return "\"" + returnValue + "\"";
    }
    private <T> T performActionInTable(String tableName, boolean append, Function<BufferedWriter, T> function) {
        //verifyTableNameIsNotEmpty(tableName);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileNameForTable(tableName), append))) {
            return function.apply(bw);
        } catch (IOException ex) {
            throw new DatabaseException("Error when creating the file: " + tableName);
        }
    }
    private void performActionInTable(String tableName, boolean append, Consumer<BufferedWriter> consumer)    {
        performActionInTable(tableName,append,bufferedWriter -> {consumer.accept(bufferedWriter);return 0;});
        //kullanılmıyor

    }
    private void writeContent(BufferedWriter bw, String text, String tableName) {
        try {
            bw.write(text);
            bw.newLine();
        } catch (IOException ex) {
            throw new DatabaseException("Unexpected error when writing content to file: " + tableName);
        }
    }
    private static int findColumnIndexForOrderBy(List<String> fields, Order order, TableData tableData) {
        if (order==null) {return 0;}
        List<String> resultColumns;
        if (!fields.isEmpty()) {
            resultColumns= fields;
        }
        else
        {
            resultColumns= tableData.getTableHeader().asListOfString();
        }
        int columnIndex = indexOfIgnoreCase(resultColumns, order.getName())
                .orElseThrow(()-> new DatabaseException("Given order by column: '"+ order.getName()+"' not found"));
        return columnIndex;
    }


    private int sortRows(Row row1, Row row2, Order order,int columnIndex) {
        if (order==null)
        {
            return 0;

        }

        String valueRow1=row1.getFields().get(columnIndex);
        String valueRow2=row2.getFields().get(columnIndex);
        if (order.getOrderType()== Order.OrderType.ASC)
        {
            return  Objects.compare(valueRow1,valueRow2, Comparator.naturalOrder());
        }
        else
        {
            return Objects.compare(valueRow1,valueRow2, Comparator.reverseOrder());
        }
    }

    private Row  selectFields(Row row, List<String> fields, TableHeader tableHeader) {
        if (fields==null || fields.isEmpty())
        {
            return row;
        } else if (fields.get(0).equals("*")) {
            return  new Row(row.getId(),row.getFields());

        } else
        {
            List<String> rowFields=fields.stream().map(field->row.getFields().get(tableHeader.indexOfColumnWithName(field)))
                    .toList();
            return  new Row(row.getId(),rowFields);
        }

    }

    private boolean applyFilters(Row row, List<Filter> filters,TableHeader tableHeader) {
        if (filters==null||filters.isEmpty())
        { return true;}
        return filters.stream().allMatch(filter ->
        {
            int columnIndex=tableHeader.indexOfColumnWithName(filter.getName());
            String valueInRow=row.getFields().get(columnIndex);
            return Objects.equals(valueInRow,filter.getValue());

        });

    }

    private  TableData  readTableData(String tableName){
        verifyTableExist(tableName);
        try (BufferedReader br = new BufferedReader(new FileReader(fileNameForTable(tableName)))) {
            TableHeader header=parseHeader(br);
            List<Row> records=new ArrayList<>();

            String line ;
            int rowId=1;
            while ((line= br.readLine()) != null )
            {
                Row record=parseLine(rowId++,line);
                records.add(record);

            }
            return new TableData(header,records);

        } catch (IOException ex) {
            throw new DatabaseException("Error when reading file:  " + tableName);
        }
    }

    private Row parseLine(int rowId,String line) {

        List<String> values=new ArrayList<>();
        StringBuilder currentValue=new StringBuilder();
        boolean inQuotes=false;
        boolean escapeNext=false;

        for (char c:line.toCharArray())
        {
            if (escapeNext)
            {
                currentValue.append(c);
                escapeNext=false;
            }
            else if (c=='\\')
            {
                escapeNext = true;
            }
            else if (c=='"')
            {
                inQuotes= !inQuotes;
            }
            else if (c==','&& !inQuotes)
            {
                values.add(currentValue.toString());
                currentValue.setLength(0); //clear
            }
            else
            {
                currentValue.append(c);
            }
        }
        values.add(currentValue.toString());

        return  new Row(rowId,values);
    }

    private TableHeader parseHeader(BufferedReader br) throws IOException {
        String line=br.readLine();
        if (line==null || line.trim().length()==0)
        {
            throw  new DatabaseException("Table file is empty");
        }
        List<Column> columns = Arrays.stream(line.split(COMMA_CHAR))
                .map(Column::stringColumn)
                .toList();
        return  new TableHeader(columns);
    }
}
