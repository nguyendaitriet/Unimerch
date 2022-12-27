package com.unimerch.service.impl;

import com.unimerch.dto.analytics.AnalyticsParam;
import com.unimerch.dto.analytics.DateFilter;
import com.unimerch.dto.order.OrderChartResult;
import com.unimerch.mapper.OrderMapper;
import com.unimerch.repository.OrderRepository;
import com.unimerch.repository.model.Order;
import com.unimerch.service.AnalyticService;
import com.unimerch.service.OrderService;
import com.unimerch.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class AnalyticServiceImpl implements AnalyticService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderChartResult getAnalyticsChart(AnalyticsParam analyticsParam) {

        return getChartAllAcc(analyticsParam);
    }


    public OrderChartResult getChartAllAcc(AnalyticsParam analyticsParam) {
        DateFilter dateFilter = DateFilter.parseDateFilter(analyticsParam.getDateFilter());
        switch (dateFilter) {
            case TODAY:
                return getChartToday();
            case YESTERDAY:
                return null;
            case THIS_MONTH:
                return null;
            case PREVIOUS_MONTH:
                return null;
            case CUSTOM:
                return null;
            default:
                return null;
        }
    }

    public OrderChartResult getChartToday() {
        List<String> dates = new ArrayList<>(Collections.singleton(TimeUtils.getCardTimeToday()));
        List<BigDecimal> royalties = new LinkedList<>();
        List<Integer> soldNumbers = new LinkedList<>();
        Instant startDate = TimeUtils.getInstantToday();
        BigDecimal royaltyPerDay = BigDecimal.ZERO;
        int soldPerDay = 0;

        List<Order> orderPerDay = orderRepository.findAllWithStartDate(startDate);

        for (Order order : orderPerDay) {
            royaltyPerDay = royaltyPerDay.add(order.getRoyalties());
            soldPerDay += order.getPurchased() - order.getCancelled();
        }

        royalties.add(royaltyPerDay);
        soldNumbers.add(soldPerDay);

        return orderMapper.toOrderChartResult(dates, royalties, soldNumbers);
    }
}
