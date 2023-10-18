package io.recepkara.project.csv.beans;

import io.recepkara.project.exception.DatabaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class TableHeader {
    private final List<Column> columns;

    public int indexOfColumnWithName(String columnName)
    {
        for (int i=0;i<columns.size();i++)
        {
            Column column=columns.get(i);
            if (columnName.equalsIgnoreCase(column.getName())){ return i;}
        }
        throw new DatabaseException("No Column found with name: "+columnName);
    }

    public List<String> asListOfString() {
       return columns.stream().map(column -> column.getName()).toList();
    }
}
