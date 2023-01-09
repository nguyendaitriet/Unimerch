package com.unimerch.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OrderCardResult {
    private String date;
    private Integer sold;
    private Integer purchased;
    private Integer cancelled;
    private Integer returned;
    private BigDecimal royalties;

    public OrderCardResult(Long sold, Long purchased, Long cancelled, Long returned, BigDecimal royalties) {
        this.sold = sold.intValue();
        this.purchased = purchased.intValue();
        this.cancelled = cancelled.intValue();
        this.returned = returned.intValue();
        this.royalties = royalties;
    }
}
