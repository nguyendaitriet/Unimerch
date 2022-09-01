package com.unimerch.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.unimerch.dto.order.OrderCardItemResult;
import com.unimerch.dto.order.OrderData;
import com.unimerch.exception.ServerErrorException;
import com.unimerch.mapper.OrderMapper;
import com.unimerch.repository.AmznAccountRepository;
import com.unimerch.repository.OrderRepository;
import com.unimerch.repository.model.AmznAccount;
import com.unimerch.repository.model.Order;
import com.unimerch.service.GroupService;
import com.unimerch.service.OrderService;
import com.unimerch.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AmznAccountRepository amznAccountRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TimeUtils timeUtils;

    public void saveOrderData(String data, String jwt) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OrderData.class, new OrderMapper());
        mapper.registerModule(module);

        AmznAccount amznAccount = amznAccountRepository.getByUsername("26");

        try {
            OrderData orderData = mapper.readValue(data, OrderData.class);

            orderData.getOrderList().forEach(order -> {
                order.setAmznAccount(amznAccount);
                orderRepository.save(order);
            });
        } catch (JsonProcessingException | ServerErrorException e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public Map<String, OrderCardItemResult> getCardsUser(Integer id) {
        Map<String, OrderCardItemResult> ordersList = new HashMap<>();

        OrderCardItemResult today = getCardUserToday(id);
        ordersList.put("today", today);

        OrderCardItemResult allTime = getCardUserAllTime(id);
        ordersList.put("allTime", allTime);

        OrderCardItemResult yesterday = getCardUserYesterday(id);
        ordersList.put("yesterday", yesterday);

        OrderCardItemResult lastSevenDay = getCardUserLastSevenDays(id);
        ordersList.put("lastSevenDay", lastSevenDay);

        OrderCardItemResult thisMonth = getCardUserThisMonth(id);
        ordersList.put("thisMonth", thisMonth);

        OrderCardItemResult previousMonth = getCardUserPreviousMonth(id);
        ordersList.put("previousMonth", previousMonth);

        return ordersList;
    }

    @Override
    public OrderCardItemResult getCardUserAllTime(Integer id) {
        List<Order> ordersAllTime = orderRepository.findByAmznAccountIdOrderByAsin(id);
        return orderMapper.toOrderCardItem(ordersAllTime, null);
    }

    @Override
    public OrderCardItemResult getCardUserToday(Integer id) {
        LocalDate today = LocalDate.now();
        ZonedDateTime zdtToday = today.atStartOfDay(timeUtils.zoneIdVN);
        Instant startTime = zdtToday.toInstant();

        List<Order> ordersToday = orderRepository.findByAmznAccIdWithStartDate(id, startTime);

        String cardTime = timeUtils.toDayMonthYear(today);
        return orderMapper.toOrderCardItem(ordersToday, cardTime);
    }

    @Override
    public OrderCardItemResult getCardUserYesterday(Integer id) {
        LocalDate today = LocalDate.now();
        LocalDate localYesterday = today.minusDays(1);
        ZonedDateTime zdtYesterdayStart = localYesterday.atStartOfDay(timeUtils.zoneIdVN);
        ZonedDateTime zdtYesterdayEnd = today.atStartOfDay(timeUtils.zoneIdVN);

        Instant startTime = zdtYesterdayStart.toInstant();
        Instant endTime = zdtYesterdayEnd.toInstant();

        List<Order> ordersYesterday = orderRepository.findByAmznAccIdWithTimeRange(id, startTime, endTime);

        String cardTime = timeUtils.toDayMonthYear(localYesterday);
        return orderMapper.toOrderCardItem(ordersYesterday, cardTime);
    }

    @Override
    public OrderCardItemResult getCardUserLastSevenDays(Integer id) {
        LocalDate lastSevenDay = LocalDate.now().minusDays(6);
        ZonedDateTime zdtLastWeek = lastSevenDay.atStartOfDay(timeUtils.zoneIdVN);
        Instant startTime = zdtLastWeek.toInstant();

        List<Order> ordersLastWeek = orderRepository.findByAmznAccIdWithStartDate(id, startTime);

        String cardTime = timeUtils.toMonthYear(lastSevenDay);
        return orderMapper.toOrderCardItem(ordersLastWeek, cardTime);
    }

    @Override
    public OrderCardItemResult getCardUserThisMonth(Integer id) {
        LocalDate firstDayOfThisMonth = LocalDate.now().withDayOfMonth(1);
        ZonedDateTime zdtThisMonth = firstDayOfThisMonth.atStartOfDay(timeUtils.zoneIdVN);
        Instant startTime = zdtThisMonth.toInstant();

        List<Order> ordersThisMonth = orderRepository.findByAmznAccIdWithStartDate(id, startTime);

        String cardTime = timeUtils.toMonthYear(firstDayOfThisMonth);
        return orderMapper.toOrderCardItem(ordersThisMonth, cardTime);
    }

    @Override
    public OrderCardItemResult getCardUserPreviousMonth(Integer id) {
        LocalDate firstDayOfLastMonth = YearMonth.now().minusMonths(1).atDay(1);
        ZonedDateTime zdtFirstDayofLastMonth = firstDayOfLastMonth.atStartOfDay(timeUtils.zoneIdVN);
        Instant startTime = zdtFirstDayofLastMonth.toInstant();

        LocalDate firstDayOfThisMonth = LocalDate.now().withDayOfMonth(1);
        ZonedDateTime zdtFirstDayOfThisMonth = firstDayOfThisMonth.atStartOfDay(timeUtils.zoneIdVN);
        Instant endTIme = zdtFirstDayOfThisMonth.toInstant();

        List<Order> ordersPreviousMonth = orderRepository.findByAmznAccIdWithTimeRange(id, startTime, endTIme);

        String cardTime = timeUtils.toMonthYear(firstDayOfLastMonth);
        return orderMapper.toOrderCardItem(ordersPreviousMonth, cardTime);
    }

    @Override
    public Map<String, OrderCardItemResult> getCardsGroup(Integer id) {
        Map<String, OrderCardItemResult> ordersList = new HashMap<>();

        return ordersList;
    }

    @Override
    public OrderCardItemResult getGetCardGroupAllTime(Integer id) {
        return null;
    }

    @Override
    public OrderCardItemResult getCardGroupToday(Integer id) {
        return null;
    }

    @Override
    public OrderCardItemResult getCardGroupYesterday(Integer id) {
        return null;
    }

    @Override
    public OrderCardItemResult getCardGroupLastWeek(Integer id) {
        return null;
    }

    @Override
    public OrderCardItemResult getCardGroupThisMonth(Integer id) {
        return null;
    }

    @Override
    public OrderCardItemResult getCardGroupPreviousMonth(Integer id) {
        return null;
    }
}