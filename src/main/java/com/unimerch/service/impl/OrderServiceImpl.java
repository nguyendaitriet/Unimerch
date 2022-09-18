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
import com.unimerch.repository.OrderRepository;
import com.unimerch.repository.OrderRepositoryExtension;
import com.unimerch.repository.ProductRepository;
import com.unimerch.repository.model.AmznUser;
import com.unimerch.repository.model.Order;
import com.unimerch.security.UserPrinciple;
import com.unimerch.service.OrderService;
import com.unimerch.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    OrderRepositoryExtension orderRepositoryExt;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MessageSource messageSource;

    public OrderData saveOrderData(String data, Authentication authentication) {

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OrderData.class, new OrderMapper());
        mapper.registerModule(module);
        //UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        //int id = Integer.parseInt(userPrinciple.getId());
        try {
            OrderData orderData = mapper.readValue(data, OrderData.class);
            List<String> orderDates = orderData.getOrderList().stream().map(order -> TimeUtils.instantToDateNoTime(order.getDate())).map(date -> TimeUtils.dateToString(date, "yyyy-MM-dd")).distinct().collect(Collectors.toList());

            orderData.getProductList().forEach(product -> {
                Optional<Order> foundOrder = orderData.getOrderList()
                        .stream()
                        .filter(order -> order.getAsin().equals(product.getId()))
                        .sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate()))
                        .findFirst();
                foundOrder.ifPresent(order -> {
                            product.setPrice(order.getRevenue()
                                    .divide(BigDecimal.valueOf(order.getPurchased())
                                    ));
                        }
                );

            });


            orderRepositoryExt.deleteAllByDate(orderDates);
            productRepository.deleteAllByIdInBatch(orderData.getAsinList());
            productRepository.saveAll(orderData.getProductList());

            orderData.getOrderList().forEach(order -> order.setAmznAccount(new AmznUser(1)));
            orderRepository.saveAll(orderData.getOrderList());

            return orderData;
        } catch (JsonProcessingException | ServerErrorException e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public int getNumberSoldInDayByAmznId(Integer amznAccId) {
        Instant startTime = TimeUtils.getInstantToday();
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
        List<String> dateList = TimeUtils.getCardsLastSevenDays();
        int columnNumber = 7;
        int dateListIndex = 0;

        while (columnNumber > 0) {
            Instant startDate = TimeUtils.getInstantLastSomeDays(columnNumber);
            Instant endDate = TimeUtils.getInstantLastSomeDays(--columnNumber);

            List<Order> orderColumn = orderRepository.findAllWithTimeRange(startDate, endDate);
            columnList.add(orderMapper.toOrderChartColumn(orderColumn, dateList.get(dateListIndex++)));
        }

        return orderMapper.toOrderChartResult(columnList);
    }

    @Override
    public OrderChartResult getChartUser(Integer amznAccId) {
        List<OrderChartColumn> columnList = new ArrayList<>();
        List<String> dateList = TimeUtils.getCardsLastSevenDays();
        int columnNumber = 7;
        int dateListIndex = 0;

        while (columnNumber > 0) {
            Instant startDate = TimeUtils.getInstantLastSomeDays(columnNumber);
            Instant endDate = TimeUtils.getInstantLastSomeDays(--columnNumber);

            List<Order> orderColumn = orderRepository.findByAmznAccIdWithTimeRange(amznAccId, startDate, endDate);
            columnList.add(orderMapper.toOrderChartColumn(orderColumn, dateList.get(dateListIndex++)));
        }

        return orderMapper.toOrderChartResult(columnList);
    }

    @Override
    public OrderChartResult getChartGroup(Integer groupId) {
        List<OrderChartColumn> columnList = new ArrayList<>();
        List<String> dateList = TimeUtils.getCardsLastSevenDays();
        int columnNumber = 7;
        int dateListIndex = 0;

        while (columnNumber > 0) {
            Instant startDate = TimeUtils.getInstantLastSomeDays(columnNumber);
            Instant endDate = TimeUtils.getInstantLastSomeDays(--columnNumber);

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
        Instant startTime = TimeUtils.getInstantToday();
        List<Order> ordersToday = orderRepository.findByAmznAccIdWithStartDate(amznAccId, startTime);

        String cardTime = TimeUtils.getCardTimeToday();
        return orderMapper.toOrderCardItem(ordersToday, cardTime);
    }

    @Override
    public OrderCardItemResult getCardUserYesterday(Integer amznAccId) {
        Map<String, Instant> timeRange = TimeUtils.getInstantYesterday();
        Instant startTime = timeRange.get("startTime");
        Instant endTime = timeRange.get("endTime");

        List<Order> ordersYesterday = orderRepository.findByAmznAccIdWithTimeRange(amznAccId, startTime, endTime);

        String cardTime = TimeUtils.getCardTimeYesterday();
        return orderMapper.toOrderCardItem(ordersYesterday, cardTime);
    }

    @Override
    public OrderCardItemResult getCardUserLastSevenDays(Integer amznAccId) {
        Instant startTime = TimeUtils.getInstantLastSomeDays(7);

        List<Order> ordersLastWeek = orderRepository.findByAmznAccIdWithStartDate(amznAccId, startTime);

        String cardTime = TimeUtils.getCardTimeLastSevenDays();
        return orderMapper.toOrderCardItem(ordersLastWeek, cardTime);
    }

    @Override
    public OrderCardItemResult getCardUserThisMonth(Integer amznAccId) {
        Instant startTime = TimeUtils.getInstantThisMonth();

        List<Order> ordersThisMonth = orderRepository.findByAmznAccIdWithStartDate(amznAccId, startTime);

        String cardTime = TimeUtils.getCardTimeThisMonth();
        return orderMapper.toOrderCardItem(ordersThisMonth, cardTime);
    }

    @Override
    public OrderCardItemResult getCardUserPreviousMonth(Integer amznAccId) {
        Map<String, Instant> timeRange = TimeUtils.getInstantPreviousMonth();
        Instant startTime = timeRange.get("startTime");
        Instant endTIme = timeRange.get("endTime");

        List<Order> ordersPreviousMonth = orderRepository.findByAmznAccIdWithTimeRange(amznAccId, startTime, endTIme);

        String cardTime = TimeUtils.getCardTimePreviousMonth();
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
        Instant startTime = TimeUtils.getInstantToday();

        List<Order> ordersToday = orderRepository.findByGroupIdWithStartDate(groupIdd, startTime);

        String cardTime = TimeUtils.getCardTimeToday();
        return orderMapper.toOrderCardItem(ordersToday, cardTime);
    }

    @Override
    public OrderCardItemResult getCardGroupYesterday(Integer groupId) {
        Map<String, Instant> timeRange = TimeUtils.getInstantYesterday();
        Instant startTime = timeRange.get("startTime");
        Instant endTime = timeRange.get("endTime");

        List<Order> ordersYesterday = orderRepository.findByGroupIdWithTimeRange(groupId, startTime, endTime);

        String cardTime = TimeUtils.getCardTimeYesterday();
        return orderMapper.toOrderCardItem(ordersYesterday, cardTime);
    }

    @Override
    public OrderCardItemResult getCardGroupLastSevenDays(Integer groupId) {
        Instant startTime = TimeUtils.getInstantLastSomeDays(7);

        List<Order> ordersLastWeek = orderRepository.findByGroupIdWithStartDate(groupId, startTime);

        String cardTime = TimeUtils.getCardTimeLastSevenDays();
        return orderMapper.toOrderCardItem(ordersLastWeek, cardTime);
    }

    @Override
    public OrderCardItemResult getCardGroupThisMonth(Integer groupId) {
        Instant startTime = TimeUtils.getInstantThisMonth();

        List<Order> ordersThisMonth = orderRepository.findByGroupIdWithStartDate(groupId, startTime);

        String cardTime = TimeUtils.getCardTimeThisMonth();
        return orderMapper.toOrderCardItem(ordersThisMonth, cardTime);
    }

    @Override
    public OrderCardItemResult getCardGroupPreviousMonth(Integer groupId) {
        Map<String, Instant> timeRange = TimeUtils.getInstantPreviousMonth();
        Instant startTime = timeRange.get("startTime");
        Instant endTIme = timeRange.get("endTime");

        List<Order> ordersPreviousMonth = orderRepository.findByGroupIdWithTimeRange(groupId, startTime, endTIme);

        String cardTime = TimeUtils.getCardTimePreviousMonth();
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
        Instant startTime = TimeUtils.getInstantToday();

        List<Order> ordersToday = orderRepository.findAllWithStartDate(startTime);

        String cardTime = TimeUtils.getCardTimeToday();
        return orderMapper.toOrderCardItem(ordersToday, cardTime);
    }

    @Override
    public OrderCardItemResult getCardAllAccYesterday() {
        Map<String, Instant> timeRange = TimeUtils.getInstantYesterday();
        Instant startTime = timeRange.get("startTime");
        Instant endTime = timeRange.get("endTime");

        List<Order> ordersYesterday = orderRepository.findAllWithTimeRange(startTime, endTime);

        String cardTime = TimeUtils.getCardTimeYesterday();
        return orderMapper.toOrderCardItem(ordersYesterday, cardTime);
    }

    @Override
    public OrderCardItemResult getCardAllAccLastSevenDays() {
        Instant startTime = TimeUtils.getInstantLastSomeDays(7);

        List<Order> ordersLastWeek = orderRepository.findAllWithStartDate(startTime);

        String cardTime = TimeUtils.getCardTimeLastSevenDays();
        return orderMapper.toOrderCardItem(ordersLastWeek, cardTime);
    }

    @Override
    public OrderCardItemResult getCardAllAccThisMonth() {
        Instant startTime = TimeUtils.getInstantThisMonth();

        List<Order> ordersThisMonth = orderRepository.findAllWithStartDate(startTime);

        String cardTime = TimeUtils.getCardTimeThisMonth();
        return orderMapper.toOrderCardItem(ordersThisMonth, cardTime);
    }

    @Override
    public OrderCardItemResult getCardAllAccPreviousMonth() {
        Map<String, Instant> timeRange = TimeUtils.getInstantPreviousMonth();
        Instant startTime = timeRange.get("startTime");
        Instant endTIme = timeRange.get("endTime");

        List<Order> ordersPreviousMonth = orderRepository.findAllWithTimeRange(startTime, endTIme);

        String cardTime = TimeUtils.getCardTimePreviousMonth();
        return orderMapper.toOrderCardItem(ordersPreviousMonth, cardTime);
    }
}