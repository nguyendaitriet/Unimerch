package com.unimerch.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsParam {
    private Integer groupId;
    private Integer amznId;
    private String dateFilter;
    private String startDate;
    private String endDate;
}
