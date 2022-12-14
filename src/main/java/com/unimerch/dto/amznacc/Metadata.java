package com.unimerch.dto.amznacc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class Metadata {
    private Integer dailyProductCount;
    private Integer dailyProductLimit;
    private Integer overallProductCount;
    private Integer overallProductLimit;
    private Integer overallDesignCount;
    private Integer overallDesignLimit;
    private Integer tier;
    private Integer totalRejected;
    private Integer totalRemoved;
}
