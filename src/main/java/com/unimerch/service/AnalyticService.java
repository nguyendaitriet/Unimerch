package com.unimerch.service;

import com.unimerch.dto.analytics.AnalyticsParam;
import com.unimerch.dto.order.OrderChartResult;

public interface AnalyticService {
    OrderChartResult getAnalyticsChart(AnalyticsParam analyticsParam);
}
