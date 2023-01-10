package com.unimerch.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.unimerch.dto.order.OrderCardResult;
import com.unimerch.dto.order.OrderChartResult;
import com.unimerch.dto.order.OrderData;
import com.unimerch.exception.ServerErrorException;
import com.unimerch.mapper.OrderMapper;
import com.unimerch.repository.amzn.AmznUserRepository;
import com.unimerch.repository.order.OrderRepository;
import com.unimerch.repository.order.OrderRepositoryExtension;
import com.unimerch.repository.product.ProductRepository;
import com.unimerch.repository.model.amzn_user.AmznUser;
import com.unimerch.repository.model.order.Order;
import com.unimerch.security.UserPrincipal;
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
    private AmznUserRepository amznUserRepository;

    @Autowired
    private MessageSource messageSource;

    public OrderData saveOrderData(String data, Authentication authentication) {

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OrderData.class, new OrderMapper());
        mapper.registerModule(module);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        int id = Integer.parseInt(principal.getId());
        try {
            OrderData orderData = mapper.readValue(data, OrderData.class);
            List<String> orderDates = orderData.getOrderList().stream()
                    .map(order -> TimeUtils.instantToDateNoTime(order.getDate()))
                    .map(date -> TimeUtils.dateToString(date, "yyyy-MM-dd"))
                    .distinct()
                    .collect(Collectors.toList());

            orderData.getProductList().forEach(product -> {
                Optional<Order> foundOrder = orderData.getOrderList()
                        .stream()
                        .filter(order -> order.getAsin().equals(product.getId()) && order.getPurchased() != 0)
                        .sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate()))
                        .findFirst();
                foundOrder.ifPresent(order ->
                        product.setPrice(order.getRevenue()
                                .divide(BigDecimal.valueOf(order.getPurchased())
                                ))
                );
            });

            orderRepositoryExt.deleteAllByDate(orderDates, id);
            productRepository.deleteAllByIdInBatch(orderData.getAsinList());
            productRepository.saveAll(orderData.getProductList());

            orderData.getOrderList().forEach(order -> order.setAmznUser(new AmznUser(id)));
            orderRepository.saveAll(orderData.getOrderList());

            amznUserRepository.updateLastCheck(id, Instant.now());
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
        List<String> dates = TimeUtils.getCardsLastSevenDays();
        List<BigDecimal> royalties = new LinkedList<>();
        List<Integer> soldNumbers = new LinkedList<>();
        int columnNumber = 7;

        while (columnNumber > 0) {
            Instant startDate = TimeUtils.getInstantLastSomeDays(columnNumber);
            Instant endDate = TimeUtils.getInstantLastSomeDays(--columnNumber);
            BigDecimal royaltyPerDay = BigDecimal.ZERO;
            int soldPerDay = 0;

            List<Order> orderPerDay = orderRepository.findAllWithTimeRange(startDate, endDate);

            for (Order order : orderPerDay) {
                royaltyPerDay = royaltyPerDay.add(order.getRoyalties());
                soldPerDay += order.getPurchased() - order.getCancelled();
            }

            royalties.add(royaltyPerDay);
            soldNumbers.add(soldPerDay);
        }

        return orderMapper.toOrderChartResult(dates, royalties, soldNumbers);
    }

    @Override
    public OrderChartResult getChartUser(Integer amznAccId) {
        List<String> dates = TimeUtils.getCardsLastSevenDays();
        List<BigDecimal> royalties = new LinkedList<>();
        List<Integer> soldNumbers = new LinkedList<>();
        int columnNumber = 7;

        while (columnNumber > 0) {
            Instant startDate = TimeUtils.getInstantLastSomeDays(columnNumber);
            Instant endDate = TimeUtils.getInstantLastSomeDays(--columnNumber);
            BigDecimal royaltyPerDay = BigDecimal.ZERO;
            int soldPerDay = 0;

            List<Order> orderPerDay = orderRepository.findByAmznAccIdWithTimeRange(amznAccId, startDate, endDate);

            for (Order order : orderPerDay) {
                royaltyPerDay = royaltyPerDay.add(order.getRoyalties());
                soldPerDay += order.getPurchased() - order.getCancelled();
            }

            royalties.add(royaltyPerDay);
            soldNumbers.add(soldPerDay);
        }

        return orderMapper.toOrderChartResult(dates, royalties, soldNumbers);
    }

    @Override
    public OrderChartResult getChartGroup(Integer groupId) {
        List<String> dates = TimeUtils.getCardsLastSevenDays();
        List<BigDecimal> royalties = new LinkedList<>();
        List<Integer> soldNumbers = new LinkedList<>();
        int columnNumber = 7;

        while (columnNumber > 0) {
            Instant startDate = TimeUtils.getInstantLastSomeDays(columnNumber);
            Instant endDate = TimeUtils.getInstantLastSomeDays(--columnNumber);
            BigDecimal royaltyPerDay = BigDecimal.ZERO;
            int soldPerDay = 0;

            List<Order> orderPerDay = orderRepository.findByGroupIdWithTimeRange(groupId, startDate, endDate);

            for (Order order : orderPerDay) {
                royaltyPerDay = royaltyPerDay.add(order.getRoyalties());
                soldPerDay += order.getPurchased() - order.getCancelled();
            }

            royalties.add(royaltyPerDay);
            soldNumbers.add(soldPerDay);
        }

        return orderMapper.toOrderChartResult(dates, royalties, soldNumbers);
    }

    @Override
    public Map<String, OrderCardResult> getCardsUser(Integer amznAccId) {
        Map<String, OrderCardResult> ordersList = new HashMap<>();

        OrderCardResult today = getCardUserToday(amznAccId);
        ordersList.put("today", today);

        OrderCardResult allTime = getCardUserAllTime(amznAccId);
        ordersList.put("allTime", allTime);

        OrderCardResult yesterday = getCardUserYesterday(amznAccId);
        ordersList.put("yesterday", yesterday);

        OrderCardResult lastSevenDays = getCardUserLastSevenDays(amznAccId);
        ordersList.put("lastSevenDays", lastSevenDays);

        OrderCardResult thisMonth = getCardUserThisMonth(amznAccId);
        ordersList.put("thisMonth", thisMonth);

        OrderCardResult previousMonth = getCardUserPreviousMonth(amznAccId);
        ordersList.put("previousMonth", previousMonth);

        return ordersList;
    }

    @Override
    public OrderCardResult getCardUserAllTime(Integer amznAccId) {
        List<Order> ordersAllTime = orderRepository.findByAmznUserId(amznAccId);
        return orderMapper.toOrderCardResult(ordersAllTime, null);
    }

    @Override
    public OrderCardResult getCardUserToday(Integer amznAccId) {
        Instant startTime = TimeUtils.getInstantToday();
        List<Order> ordersToday = orderRepository.findByAmznAccIdWithStartDate(amznAccId, startTime);

        String cardTime = TimeUtils.getCardTimeToday();
        return orderMapper.toOrderCardResult(ordersToday, cardTime);
    }

    @Override
    public OrderCardResult getCardUserYesterday(Integer amznAccId) {
        Map<String, Instant> timeRange = TimeUtils.getInstantYesterday();
        Instant startTime = timeRange.get("startTime");
        Instant endTime = timeRange.get("endTime");

        List<Order> ordersYesterday = orderRepository.findByAmznAccIdWithTimeRange(amznAccId, startTime, endTime);

        String cardTime = TimeUtils.getCardTimeYesterday();
        return orderMapper.toOrderCardResult(ordersYesterday, cardTime);
    }

    @Override
    public OrderCardResult getCardUserLastSevenDays(Integer amznAccId) {
        Instant startTime = TimeUtils.getInstantLastSomeDays(7);

        List<Order> ordersLastWeek = orderRepository.findByAmznAccIdWithStartDate(amznAccId, startTime);

        String cardTime = TimeUtils.getCardTimeLastSevenDays();
        return orderMapper.toOrderCardResult(ordersLastWeek, cardTime);
    }

    @Override
    public OrderCardResult getCardUserThisMonth(Integer amznAccId) {
        Instant startTime = TimeUtils.getInstantThisMonth();

        List<Order> ordersThisMonth = orderRepository.findByAmznAccIdWithStartDate(amznAccId, startTime);

        String cardTime = TimeUtils.getCardTimeThisMonth();
        return orderMapper.toOrderCardResult(ordersThisMonth, cardTime);
    }

    @Override
    public OrderCardResult getCardUserPreviousMonth(Integer amznAccId) {
        Map<String, Instant> timeRange = TimeUtils.getInstantPreviousMonth();
        Instant startTime = timeRange.get("startTime");
        Instant endTIme = timeRange.get("endTime");

        List<Order> ordersPreviousMonth = orderRepository.findByAmznAccIdWithTimeRange(amznAccId, startTime, endTIme);

        String cardTime = TimeUtils.getCardTimePreviousMonth();
        return orderMapper.toOrderCardResult(ordersPreviousMonth, cardTime);
    }

    @Override
    public Map<String, OrderCardResult> getCardsGroup(Integer groupId) {
        Map<String, OrderCardResult> ordersList = new HashMap<>();

        OrderCardResult today = getCardGroupToday(groupId);
        ordersList.put("today", today);

        OrderCardResult allTime = getCardGroupAllTime(groupId);
        ordersList.put("allTime", allTime);

        OrderCardResult yesterday = getCardGroupYesterday(groupId);
        ordersList.put("yesterday", yesterday);

        OrderCardResult lastSevenDays = getCardGroupLastSevenDays(groupId);
        ordersList.put("lastSevenDays", lastSevenDays);

        OrderCardResult thisMonth = getCardGroupThisMonth(groupId);
        ordersList.put("thisMonth", thisMonth);

        OrderCardResult previousMonth = getCardGroupPreviousMonth(groupId);
        ordersList.put("previousMonth", previousMonth);

        return ordersList;
    }

    @Override
    public OrderCardResult getCardGroupAllTime(Integer groupId) {
        List<Order> orderList = orderRepository.findByGroupId(groupId);
        return orderMapper.toOrderCardResult(orderList, null);
    }

    @Override
    public OrderCardResult getCardGroupToday(Integer groupIdd) {
        Instant startTime = TimeUtils.getInstantToday();

        List<Order> ordersToday = orderRepository.findByGroupIdWithStartDate(groupIdd, startTime);

        String cardTime = TimeUtils.getCardTimeToday();
        return orderMapper.toOrderCardResult(ordersToday, cardTime);
    }

    @Override
    public OrderCardResult getCardGroupYesterday(Integer groupId) {
        Map<String, Instant> timeRange = TimeUtils.getInstantYesterday();
        Instant startTime = timeRange.get("startTime");
        Instant endTime = timeRange.get("endTime");

        List<Order> ordersYesterday = orderRepository.findByGroupIdWithTimeRange(groupId, startTime, endTime);

        String cardTime = TimeUtils.getCardTimeYesterday();
        return orderMapper.toOrderCardResult(ordersYesterday, cardTime);
    }

    @Override
    public OrderCardResult getCardGroupLastSevenDays(Integer groupId) {
        Instant startTime = TimeUtils.getInstantLastSomeDays(7);

        List<Order> ordersLastWeek = orderRepository.findByGroupIdWithStartDate(groupId, startTime);

        String cardTime = TimeUtils.getCardTimeLastSevenDays();
        return orderMapper.toOrderCardResult(ordersLastWeek, cardTime);
    }

    @Override
    public OrderCardResult getCardGroupThisMonth(Integer groupId) {
        Instant startTime = TimeUtils.getInstantThisMonth();

        List<Order> ordersThisMonth = orderRepository.findByGroupIdWithStartDate(groupId, startTime);

        String cardTime = TimeUtils.getCardTimeThisMonth();
        return orderMapper.toOrderCardResult(ordersThisMonth, cardTime);
    }

    @Override
    public OrderCardResult getCardGroupPreviousMonth(Integer groupId) {
        Map<String, Instant> timeRange = TimeUtils.getInstantPreviousMonth();
        Instant startTime = timeRange.get("startTime");
        Instant endTIme = timeRange.get("endTime");

        List<Order> ordersPreviousMonth = orderRepository.findByGroupIdWithTimeRange(groupId, startTime, endTIme);

        String cardTime = TimeUtils.getCardTimePreviousMonth();
        return orderMapper.toOrderCardResult(ordersPreviousMonth, cardTime);
    }

    @Override
    public Map<String, OrderCardResult> getCardAllAcc() {
        Map<String, OrderCardResult> ordersList = new HashMap<>();

        OrderCardResult today = getCardAllAccToday();
        ordersList.put("today", today);

        OrderCardResult allTime = getCardAllAccAllTime();
        ordersList.put("allTime", allTime);

        OrderCardResult yesterday = getCardAllAccYesterday();
        ordersList.put("yesterday", yesterday);

        OrderCardResult lastSevenDays = getCardAllAccLastSevenDays();
        ordersList.put("lastSevenDays", lastSevenDays);

        OrderCardResult thisMonth = getCardAllAccThisMonth();
        ordersList.put("thisMonth", thisMonth);

        OrderCardResult previousMonth = getCardAllAccPreviousMonth();
        ordersList.put("previousMonth", previousMonth);

        return ordersList;
    }

    @Override
    public OrderCardResult getCardAllAccAllTime() {
        List<Order> ordersList = orderRepository.findAll();
        return orderMapper.toOrderCardResult(ordersList, null);
    }

    @Override
    public OrderCardResult getCardAllAccToday() {
        Instant startTime = TimeUtils.getInstantToday();

        List<Order> ordersToday = orderRepository.findAllWithStartDate(startTime);

        String cardTime = TimeUtils.getCardTimeToday();
        return orderMapper.toOrderCardResult(ordersToday, cardTime);
    }

    @Override
    public OrderCardResult getCardAllAccYesterday() {
        Map<String, Instant> timeRange = TimeUtils.getInstantYesterday();
        Instant startTime = timeRange.get("startTime");
        Instant endTime = timeRange.get("endTime");

        List<Order> ordersYesterday = orderRepository.findAllWithTimeRange(startTime, endTime);

        String cardTime = TimeUtils.getCardTimeYesterday();
        return orderMapper.toOrderCardResult(ordersYesterday, cardTime);
    }

    @Override
    public OrderCardResult getCardAllAccLastSevenDays() {
        Instant startTime = TimeUtils.getInstantLastSomeDays(7);

        List<Order> ordersLastWeek = orderRepository.findAllWithStartDate(startTime);

        String cardTime = TimeUtils.getCardTimeLastSevenDays();
        return orderMapper.toOrderCardResult(ordersLastWeek, cardTime);
    }

    @Override
    public OrderCardResult getCardAllAccThisMonth() {
        Instant startTime = TimeUtils.getInstantThisMonth();

        List<Order> ordersThisMonth = orderRepository.findAllWithStartDate(startTime);

        String cardTime = TimeUtils.getCardTimeThisMonth();
        return orderMapper.toOrderCardResult(ordersThisMonth, cardTime);
    }

    @Override
    public OrderCardResult getCardAllAccPreviousMonth() {
        Map<String, Instant> timeRange = TimeUtils.getInstantPreviousMonth();
        Instant startTime = timeRange.get("startTime");
        Instant endTIme = timeRange.get("endTime");

        List<Order> ordersPreviousMonth = orderRepository.findAllWithTimeRange(startTime, endTIme);

        String cardTime = TimeUtils.getCardTimePreviousMonth();
        return orderMapper.toOrderCardResult(ordersPreviousMonth, cardTime);
    }
}