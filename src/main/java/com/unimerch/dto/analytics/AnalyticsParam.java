package com.unimerch.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    private boolean searchable;
    private String searchKey;
    private List<String> tagIncluded;
    private List<String> tagExcluded;
}
