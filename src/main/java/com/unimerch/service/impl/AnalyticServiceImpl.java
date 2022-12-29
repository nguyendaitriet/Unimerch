package com.unimerch.service.impl;

import com.unimerch.dto.analytics.AnalyticsParam;
import com.unimerch.dto.analytics.DateFilter;
import com.unimerch.dto.order.OrderChartColumn;
import com.unimerch.dto.order.OrderChartResult;
import com.unimerch.exception.ServerErrorException;
import com.unimerch.mapper.OrderMapper;
import com.unimerch.repository.OrderRepository;
import com.unimerch.service.AnalyticService;
import com.unimerch.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
    @Autowired
    private MessageSource messageSource;

    @Override
    public OrderChartResult getAnalyticsChart(AnalyticsParam analyticsParam) {
        DateFilter dateFilter = DateFilter.parseDateFilter(analyticsParam.getDateFilter());
        int groupId = analyticsParam.getGroupId();
        int amznId = analyticsParam.getAmznId();
        String amznFilter = groupId == 0 && amznId == 0 ? "all" : groupId != 0 && amznId == 0? "group" : "amzn";

        switch (dateFilter) {
            case TODAY:
                return getChartToday(analyticsParam, amznFilter);
            case YESTERDAY:
                return getChartYesterday(analyticsParam, amznFilter);
            case THIS_MONTH:
                return getChartThisMonth(analyticsParam, amznFilter);
            case PREVIOUS_MONTH:
                return getChartPreviousMonth(analyticsParam, amznFilter);
            case CUSTOM:
                return getChartCustomDateRange(analyticsParam, amznFilter);
            default:
                throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    public OrderChartResult getChartToday(AnalyticsParam analyticsParam, String amznFilter) {
        Instant startDate = TimeUtils.getInstantToday();
        Instant endDate = Instant.now();
        List<OrderChartColumn> orderChartColumnList = getOrderChartColumnList(analyticsParam, amznFilter, startDate, endDate);

        return processOrderChartList(orderChartColumnList);
    }

    public OrderChartResult getChartYesterday(AnalyticsParam analyticsParam, String amznFilter) {
        Map<String, Instant> instantYesterday = TimeUtils.getInstantYesterday();
        Instant startDate = instantYesterday.get("startTime");
        Instant endDate = instantYesterday.get("endTime");
        List<OrderChartColumn> orderChartColumnList = getOrderChartColumnList(analyticsParam, amznFilter, startDate, endDate);

        return processOrderChartList(orderChartColumnList);
    }

    public OrderChartResult getChartThisMonth(AnalyticsParam analyticsParam, String amznFilter) {
        Instant startDate = TimeUtils.getInstantThisMonth();
        Instant endDate = Instant.now();
        List<OrderChartColumn> orderChartColumnList = getOrderChartColumnList(analyticsParam, amznFilter, startDate, endDate);

        return processOrderChartList(orderChartColumnList);
    }

    public OrderChartResult getChartPreviousMonth(AnalyticsParam analyticsParam, String amznFilter) {
        Map<String, Instant> instantPreviousMonth = TimeUtils.getInstantPreviousMonth();
        Instant startDate = instantPreviousMonth.get("startTime");
        Instant endDate = instantPreviousMonth.get("endTime");
        List<OrderChartColumn> orderChartColumnList = getOrderChartColumnList(analyticsParam, amznFilter, startDate, endDate);

        return processOrderChartList(orderChartColumnList);
    }

    public OrderChartResult getChartCustomDateRange(AnalyticsParam analyticsParam, String amznFilter) {
        Instant startDate = TimeUtils.convertStringToInstant(analyticsParam.getStartDate(), TimeUtils.dayMonthYearPattern);
        Instant endDate = TimeUtils.convertStringToInstant(analyticsParam.getEndDate(), TimeUtils.dayMonthYearPattern).plus(Period.ofDays(1));
        List<OrderChartColumn> orderChartColumnList = getOrderChartColumnList(analyticsParam, amznFilter, startDate, endDate);

        return processOrderChartList(orderChartColumnList);
    }

    public List<OrderChartColumn> getOrderChartColumnList(AnalyticsParam analyticsParam, String amznFilter, Instant startDate, Instant endDate) {
        switch (amznFilter) {
            case "all":
                return orderRepository.findAllOrderChartWithDateRange(startDate, endDate);
            case "group":
                return orderRepository.findGroupOrderChartWithDateRange(analyticsParam.getGroupId(), startDate, endDate);
            case "amzn":
                return orderRepository.findAmznOrderChartWithDateRange(analyticsParam.getAmznId(), startDate, endDate);
            default:
                throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    public OrderChartResult processOrderChartList(List<OrderChartColumn> orderChartColumnList) {
        List<String> dates = new LinkedList<>();
        List<BigDecimal> royalties = new LinkedList<>();
        List<Integer> soldNumbers = new LinkedList<>();

        orderChartColumnList.forEach(item -> {
            dates.add(item.getDate());
            royalties.add(item.getRoyalties());
            soldNumbers.add(item.getSold());
        });

        return orderMapper.toOrderChartResult(dates, royalties, soldNumbers);
    }

}
