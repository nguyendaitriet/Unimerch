package com.unimerch.service;

import com.unimerch.dto.order.OrderCardItemResult;
import com.unimerch.dto.order.OrderData;

import java.util.Map;

public interface OrderService {
    OrderData saveOrderData(String data, String jwt);

    Map<String, OrderCardItemResult> getCardsUser(Integer id);

    OrderCardItemResult getCardUserAllTime(Integer id);

    OrderCardItemResult getCardUserToday(Integer id);

    OrderCardItemResult getCardUserYesterday(Integer id);

    OrderCardItemResult getCardUserLastSevenDays(Integer id);

    OrderCardItemResult getCardUserThisMonth(Integer id);

    OrderCardItemResult getCardUserPreviousMonth(Integer id);

    Map<String, OrderCardItemResult> getCardsGroup(Integer id);

    OrderCardItemResult getGetCardGroupAllTime(Integer id);

    OrderCardItemResult getCardGroupToday(Integer id);

    OrderCardItemResult getCardGroupYesterday(Integer id);

    OrderCardItemResult getCardGroupLastWeek(Integer id);

    OrderCardItemResult getCardGroupThisMonth(Integer id);

    OrderCardItemResult getCardGroupPreviousMonth(Integer id);
}
