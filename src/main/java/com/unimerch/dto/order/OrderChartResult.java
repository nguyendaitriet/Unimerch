package com.unimerch.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OrderChartResult {
    private int maxRoyalties;

    private int intervalRoyalties;

    private int maxSold;

    private int intervalSold;

    private List<OrderChartColumn> columns;
}