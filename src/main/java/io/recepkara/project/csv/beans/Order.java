package io.recepkara.project.csv.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Order {
    private  final  String name;
    private  final OrderType orderType;

            public enum OrderType
            {
                ASC,
                DESC
            }
}
