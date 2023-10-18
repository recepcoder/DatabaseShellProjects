package io.recepkara.project.utils;

import java.util.List;
import java.util.Optional;

public class ListUtils {
    public static  Optional<Integer> indexOfIgnoreCase(List<String> list, String value)
    {
        for (int i=0;i<list.size();i++)
        {
           String listValue=list.get(i);
            if (listValue.equalsIgnoreCase(value)){ return Optional.of(i);}
        }
       return Optional.empty();
    }
}
