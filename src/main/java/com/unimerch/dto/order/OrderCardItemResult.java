package com.unimerch.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OrderCardItemResult {
    private String date;

    private Integer numberSold;

    private Integer purchased;

    private Integer cancelled;

    private Integer returned;

    private BigDecimal royalties;
}
