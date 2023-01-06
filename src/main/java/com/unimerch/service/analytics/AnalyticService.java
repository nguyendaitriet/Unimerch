package com.unimerch.service.analytics;

import com.unimerch.dto.analytics.AnalyticsParam;
import com.unimerch.dto.analytics.ProductAnalyticsResult;
import com.unimerch.dto.order.OrderChartResult;

import java.util.List;

public interface AnalyticService {
    OrderChartResult getAnalyticsChart(AnalyticsParam analyticsParam);
    List<ProductAnalyticsResult> getProductAnalyticsList(AnalyticsParam analyticsParam);
}
