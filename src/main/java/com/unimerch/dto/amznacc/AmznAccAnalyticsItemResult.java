package com.unimerch.dto.amznacc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class AmznAccAnalyticsItemResult {
    private Integer id;
    private String username;
    private Integer published;
    private Integer tier;
    private Integer slotRemaining;
    private Integer slotTotal;
    private Integer reject;
    private Integer remove;
    private Long sales;
    private String note;
}