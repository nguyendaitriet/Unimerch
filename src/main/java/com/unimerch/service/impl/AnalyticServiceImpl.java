package com.unimerch.service.impl;

import com.unimerch.dto.analytics.AnalyticsParam;
import com.unimerch.dto.analytics.DateFilter;
import com.unimerch.dto.order.OrderChartColumn;
import com.unimerch.dto.order.OrderChartResult;
import com.unimerch.mapper.OrderMapper;
import com.unimerch.repository.OrderRepository;
import com.unimerch.repository.model.Order;
import com.unimerch.service.AnalyticService;
import com.unimerch.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.Period;
import java.util.*;

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
                return getChartYesterday();
            case THIS_MONTH:
                return getChartThisMonth();
            case PREVIOUS_MONTH:
                return getChartPreviousMonth();
            case CUSTOM:
                return getChartCustomDateRange(analyticsParam);
            default:
                return null;
        }
    }

    public OrderChartResult getChartToday() {
        List<String> dates = new ArrayList<>(Collections.singleton(TimeUtils.getCardTimeToday()));
        Instant startDate = TimeUtils.getInstantToday();
        List<Order> orderList = orderRepository.findAllWithStartDate(startDate);
        return processOrderChartWithOneColumn(orderList, dates);
    }

    public OrderChartResult getChartYesterday() {
        List<String> dates = new ArrayList<>(Collections.singleton(TimeUtils.getCardTimeYesterday()));
        Map<String, Instant> instantYesterday = TimeUtils.getInstantYesterday();
        List<Order> orderList = orderRepository.findAllWithTimeRange(instantYesterday.get("startTime"), instantYesterday.get("endTime"));
        return processOrderChartWithOneColumn(orderList, dates);
    }

    public OrderChartResult getChartThisMonth() {
        Instant firstDateOfThisMonth = TimeUtils.getInstantThisMonth();
        Instant today = Instant.now();
        return processOrderChartWithMultiColumn(firstDateOfThisMonth, today);
    }

    public OrderChartResult getChartPreviousMonth() {
        Map<String, Instant> instantPreviousMonth = TimeUtils.getInstantPreviousMonth();
        return processOrderChartWithMultiColumn(instantPreviousMonth.get("startTime"), instantPreviousMonth.get("endTime"));
    }

    public OrderChartResult getChartCustomDateRange(AnalyticsParam analyticsParam) {
        Instant startDate = TimeUtils.convertStringToInstant(analyticsParam.getStartDate(), TimeUtils.dayMonthYearPattern);
        Instant endDate = TimeUtils.convertStringToInstant(analyticsParam.getEndDate(), TimeUtils.dayMonthYearPattern).plus(Period.ofDays(1));
        return processOrderChartWithMultiColumn(startDate, endDate);
    }

    public OrderChartResult processOrderChartWithOneColumn(List<Order> orderList, List<String> dates) {
        List<BigDecimal> royalties = new LinkedList<>();
        List<Integer> soldNumbers = new LinkedList<>();
        BigDecimal royaltyPerDay = BigDecimal.ZERO;
        int soldPerDay = 0;

        for (Order order : orderList) {
            royaltyPerDay = royaltyPerDay.add(order.getRoyalties());
            soldPerDay += order.getPurchased() - order.getCancelled();
        }

        royalties.add(royaltyPerDay);
        soldNumbers.add(soldPerDay);

        return orderMapper.toOrderChartResult(dates, royalties, soldNumbers);
    }

    public OrderChartResult processOrderChartWithMultiColumn(Instant startDay, Instant endDay) {
        List<String> dates = new LinkedList<>();
        List<BigDecimal> royalties = new LinkedList<>();
        List<Integer> soldNumbers = new LinkedList<>();

        List<OrderChartColumn> orderChartColumnList = orderRepository.findAllOrderChartWithDateRange(startDay, endDay);

        orderChartColumnList.forEach(item -> {
            dates.add(item.getDate());
            royalties.add(item.getRoyalties());
            soldNumbers.add(item.getSold());
        });

        return orderMapper.toOrderChartResult(dates, royalties, soldNumbers);
    }

}
