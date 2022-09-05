package com.unimerch.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.unimerch.dto.order.OrderCardItemResult;
import com.unimerch.dto.order.OrderChartColumn;
import com.unimerch.dto.order.OrderChartResult;
import com.unimerch.dto.order.OrderData;
import com.unimerch.exception.ServerErrorException;
import com.unimerch.mapper.OrderMapper;
import com.unimerch.repository.AmznAccountRepository;
import com.unimerch.repository.OrderRepository;
import com.unimerch.repository.model.AmznAccount;
import com.unimerch.repository.model.Order;
import com.unimerch.service.OrderService;
import com.unimerch.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AmznAccountRepository amznAccountRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TimeUtils timeUtils;

    public OrderData saveOrderData(String data, String jwt) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OrderData.class, new OrderMapper());
        mapper.registerModule(module);

        AmznAccount amznAccount = amznAccountRepository.getByUsername("1");

        try {
            OrderData orderData = mapper.readValue(data, OrderData.class);

            orderData.getOrderList().forEach(order -> {
                order.setAmznAccount(amznAccount);
                orderRepository.save(order);
            });

            return orderData;
        } catch (JsonProcessingException | ServerErrorException e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public int getNumberSoldInDayByAmznId(Integer amznAccId) {
        Instant startTime = timeUtils.getInstantToday();
        List<Order> orderList = orderRepository.findByAmznAccIdWithStartDate(amznAccId, startTime);
        int numberSold = 0;

        for (Order order : orderList) {
            numberSold += order.getPurchased() - order.getCancelled();
        }

        return numberSold;
    }

    @Override
    public OrderChartResult getChartAllAcc() {
        List<OrderChartColumn> columnList = new ArrayList<>();
        List<String> dateList = timeUtils.getCardsLastSevenDays();
        int columnNumber = 7;
        int dateListIndex = 0;

        while (columnNumber > 0) {
            Instant startDate = timeUtils.getInstantLastSomeDays(columnNumber);
            Instant endDate = timeUtils.getInstantLastSomeDays(--columnNumber);

            List<Order> orderColumn = orderRepository.findAllWithTimeRange(startDate, endDate);
            columnList.add(orderMapper.toOrderChartColumn(orderColumn, dateList.get(dateListIndex++)));
        }

        return orderMapper.toOrderChartResult(columnList);
    }

    @Override
    public OrderChartResult getChartUser(Integer amznAccId) {
        List<OrderChartColumn> columnList = new ArrayList<>();
        List<String> dateList = timeUtils.getCardsLastSevenDays();
        int columnNumber = 7;
        int dateListIndex = 0;

        while (columnNumber > 0) {
            Instant startDate = timeUtils.getInstantLastSomeDays(columnNumber);
            Instant endDate = timeUtils.getInstantLastSomeDays(--columnNumber);

            List<Order> orderColumn = orderRepository.findByAmznAccIdWithTimeRange(amznAccId, startDate, endDate);
            columnList.add(orderMapper.toOrderChartColumn(orderColumn, dateList.get(dateListIndex++)));
        }

        return orderMapper.toOrderChartResult(columnList);
    }

    @Override
    public OrderChartResult getChartGroup(Integer groupId) {
        List<OrderChartColumn> columnList = new ArrayList<>();
        List<String> dateList = timeUtils.getCardsLastSevenDays();
        int columnNumber = 7;
        int dateListIndex = 0;

        while (columnNumber > 0) {
            Instant startDate = timeUtils.getInstantLastSomeDays(columnNumber);
            Instant endDate = timeUtils.getInstantLastSomeDays(--columnNumber);

            List<Order> orderColumn = orderRepository.findByGroupIdWithTimeRange(groupId, startDate, endDate);
            columnList.add(orderMapper.toOrderChartColumn(orderColumn, dateList.get(dateListIndex++)));
        }

        return orderMapper.toOrderChartResult(columnList);
    }

    @Override
    public Map<String, OrderCardItemResult> getCardsUser(Integer amznAccId) {
        Map<String, OrderCardItemResult> ordersList = new HashMap<>();

        OrderCardItemResult today = getCardUserToday(amznAccId);
        ordersList.put("today", today);

        OrderCardItemResult allTime = getCardUserAllTime(amznAccId);
        ordersList.put("allTime", allTime);

        OrderCardItemResult yesterday = getCardUserYesterday(amznAccId);
        ordersList.put("yesterday", yesterday);

        OrderCardItemResult lastSevenDays = getCardUserLastSevenDays(amznAccId);
        ordersList.put("lastSevenDays", lastSevenDays);

        OrderCardItemResult thisMonth = getCardUserThisMonth(amznAccId);
        ordersList.put("thisMonth", thisMonth);

        OrderCardItemResult previousMonth = getCardUserPreviousMonth(amznAccId);
        ordersList.put("previousMonth", previousMonth);

        return ordersList;
    }

    @Override
    public OrderCardItemResult getCardUserAllTime(Integer amznAccId) {
        List<Order> ordersAllTime = orderRepository.findByAmznAccountId(amznAccId);
        return orderMapper.toOrderCardItem(ordersAllTime, null);
    }

    @Override
    public OrderCardItemResult getCardUserToday(Integer amznAccId) {
        Instant startTime = timeUtils.getInstantToday();
        List<Order> ordersToday = orderRepository.findByAmznAccIdWithStartDate(amznAccId, startTime);

        String cardTime = timeUtils.getCardTimeToday();
        return orderMapper.toOrderCardItem(ordersToday, cardTime);
    }

    @Override
    public OrderCardItemResult getCardUserYesterday(Integer amznAccId) {
        Map<String, Instant> timeRange = timeUtils.getInstantYesterday();
        Instant startTime = timeRange.get("startTime");
        Instant endTime = timeRange.get("endTime");

        List<Order> ordersYesterday = orderRepository.findByAmznAccIdWithTimeRange(amznAccId, startTime, endTime);

        String cardTime = timeUtils.getCardTimeYesterday();
        return orderMapper.toOrderCardItem(ordersYesterday, cardTime);
    }

    @Override
    public OrderCardItemResult getCardUserLastSevenDays(Integer amznAccId) {
        Instant startTime = timeUtils.getInstantLastSomeDays(7);

        List<Order> ordersLastWeek = orderRepository.findByAmznAccIdWithStartDate(amznAccId, startTime);

        String cardTime = timeUtils.getCardTimeLastSevenDays();
        return orderMapper.toOrderCardItem(ordersLastWeek, cardTime);
    }

    @Override
    public OrderCardItemResult getCardUserThisMonth(Integer amznAccId) {
        Instant startTime = timeUtils.getInstantThisMonth();

        List<Order> ordersThisMonth = orderRepository.findByAmznAccIdWithStartDate(amznAccId, startTime);

        String cardTime = timeUtils.getCardTimeThisMonth();
        return orderMapper.toOrderCardItem(ordersThisMonth, cardTime);
    }

    @Override
    public OrderCardItemResult getCardUserPreviousMonth(Integer amznAccId) {
        Map<String, Instant> timeRange = timeUtils.getInstantPreviousMonth();
        Instant startTime = timeRange.get("startTime");
        Instant endTIme = timeRange.get("endTime");

        List<Order> ordersPreviousMonth = orderRepository.findByAmznAccIdWithTimeRange(amznAccId, startTime, endTIme);

        String cardTime = timeUtils.getCardTimePreviousMonth();
        return orderMapper.toOrderCardItem(ordersPreviousMonth, cardTime);
    }

    @Override
    public Map<String, OrderCardItemResult> getCardsGroup(Integer groupId) {
        Map<String, OrderCardItemResult> ordersList = new HashMap<>();

        OrderCardItemResult today = getCardGroupToday(groupId);
        ordersList.put("today", today);

        OrderCardItemResult allTime = getCardGroupAllTime(groupId);
        ordersList.put("allTime", allTime);

        OrderCardItemResult yesterday = getCardGroupYesterday(groupId);
        ordersList.put("yesterday", yesterday);

        OrderCardItemResult lastSevenDays = getCardGroupLastSevenDays(groupId);
        ordersList.put("lastSevenDays", lastSevenDays);

        OrderCardItemResult thisMonth = getCardGroupThisMonth(groupId);
        ordersList.put("thisMonth", thisMonth);

        OrderCardItemResult previousMonth = getCardGroupPreviousMonth(groupId);
        ordersList.put("previousMonth", previousMonth);

        return ordersList;
    }

    @Override
    public OrderCardItemResult getCardGroupAllTime(Integer groupId) {
        List<Order> orderList = orderRepository.findByGroupId(groupId);
        return orderMapper.toOrderCardItem(orderList, null);
    }

    @Override
    public OrderCardItemResult getCardGroupToday(Integer groupIdd) {
        Instant startTime = timeUtils.getInstantToday();

        List<Order> ordersToday = orderRepository.findByGroupIdWithStartDate(groupIdd, startTime);

        String cardTime = timeUtils.getCardTimeToday();
        return orderMapper.toOrderCardItem(ordersToday, cardTime);
    }

    @Override
    public OrderCardItemResult getCardGroupYesterday(Integer groupId) {
        Map<String, Instant> timeRange = timeUtils.getInstantYesterday();
        Instant startTime = timeRange.get("startTime");
        Instant endTime = timeRange.get("endTime");

        List<Order> ordersYesterday = orderRepository.findByGroupIdWithTimeRange(groupId, startTime, endTime);

        String cardTime = timeUtils.getCardTimeYesterday();
        return orderMapper.toOrderCardItem(ordersYesterday, cardTime);
    }

    @Override
    public OrderCardItemResult getCardGroupLastSevenDays(Integer groupId) {
        Instant startTime = timeUtils.getInstantLastSomeDays(7);

        List<Order> ordersLastWeek = orderRepository.findByGroupIdWithStartDate(groupId, startTime);

        String cardTime = timeUtils.getCardTimeLastSevenDays();
        return orderMapper.toOrderCardItem(ordersLastWeek, cardTime);
    }

    @Override
    public OrderCardItemResult getCardGroupThisMonth(Integer groupId) {
        Instant startTime = timeUtils.getInstantThisMonth();

        List<Order> ordersThisMonth = orderRepository.findByGroupIdWithStartDate(groupId, startTime);

        String cardTime = timeUtils.getCardTimeThisMonth();
        return orderMapper.toOrderCardItem(ordersThisMonth, cardTime);
    }

    @Override
    public OrderCardItemResult getCardGroupPreviousMonth(Integer groupId) {
        Map<String, Instant> timeRange = timeUtils.getInstantPreviousMonth();
        Instant startTime = timeRange.get("startTime");
        Instant endTIme = timeRange.get("endTime");

        List<Order> ordersPreviousMonth = orderRepository.findByGroupIdWithTimeRange(groupId, startTime, endTIme);

        String cardTime = timeUtils.getCardTimePreviousMonth();
        return orderMapper.toOrderCardItem(ordersPreviousMonth, cardTime);
    }

    @Override
    public Map<String, OrderCardItemResult> getCardAllAcc() {
        Map<String, OrderCardItemResult> ordersList = new HashMap<>();

        OrderCardItemResult today = getCardAllAccToday();
        ordersList.put("today", today);

        OrderCardItemResult allTime = getCardAllAccAllTime();
        ordersList.put("allTime", allTime);

        OrderCardItemResult yesterday = getCardAllAccYesterday();
        ordersList.put("yesterday", yesterday);

        OrderCardItemResult lastSevenDays = getCardAllAccLastSevenDays();
        ordersList.put("lastSevenDays", lastSevenDays);

        OrderCardItemResult thisMonth = getCardAllAccThisMonth();
        ordersList.put("thisMonth", thisMonth);

        OrderCardItemResult previousMonth = getCardAllAccPreviousMonth();
        ordersList.put("previousMonth", previousMonth);

        return ordersList;
    }

    @Override
    public OrderCardItemResult getCardAllAccAllTime() {
        List<Order> ordersList = orderRepository.findAll();
        return orderMapper.toOrderCardItem(ordersList, null);
    }

    @Override
    public OrderCardItemResult getCardAllAccToday() {
        Instant startTime = timeUtils.getInstantToday();

        List<Order> ordersToday = orderRepository.findAllWithStartDate(startTime);

        String cardTime = timeUtils.getCardTimeToday();
        return orderMapper.toOrderCardItem(ordersToday, cardTime);
    }

    @Override
    public OrderCardItemResult getCardAllAccYesterday() {
        Map<String, Instant> timeRange = timeUtils.getInstantYesterday();
        Instant startTime = timeRange.get("startTime");
        Instant endTime = timeRange.get("endTime");

        List<Order> ordersYesterday = orderRepository.findAllWithTimeRange(startTime, endTime);

        String cardTime = timeUtils.getCardTimeYesterday();
        return orderMapper.toOrderCardItem(ordersYesterday, cardTime);
    }

    @Override
    public OrderCardItemResult getCardAllAccLastSevenDays() {
        Instant startTime = timeUtils.getInstantLastSomeDays(7);

        List<Order> ordersLastWeek = orderRepository.findAllWithStartDate(startTime);

        String cardTime = timeUtils.getCardTimeLastSevenDays();
        return orderMapper.toOrderCardItem(ordersLastWeek, cardTime);
    }

    @Override
    public OrderCardItemResult getCardAllAccThisMonth() {
        Instant startTime = timeUtils.getInstantThisMonth();

        List<Order> ordersThisMonth = orderRepository.findAllWithStartDate(startTime);

        String cardTime = timeUtils.getCardTimeThisMonth();
        return orderMapper.toOrderCardItem(ordersThisMonth, cardTime);
    }

    @Override
    public OrderCardItemResult getCardAllAccPreviousMonth() {
        Map<String, Instant> timeRange = timeUtils.getInstantPreviousMonth();
        Instant startTime = timeRange.get("startTime");
        Instant endTIme = timeRange.get("endTime");

        List<Order> ordersPreviousMonth = orderRepository.findAllWithTimeRange(startTime, endTIme);

        String cardTime = timeUtils.getCardTimePreviousMonth();
        return orderMapper.toOrderCardItem(ordersPreviousMonth, cardTime);
    }
}