package io.recepkara.project.csv.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Column {
    public  static  final  String STRING_COLUMN="STRING";
    private  final  String name;
    private  final String type;

    public  static  Column stringColumn(String name)
    {
        return  new Column(name,STRING_COLUMN);
    }
}
