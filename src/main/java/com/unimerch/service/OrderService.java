package com.unimerch.service;

import com.unimerch.dto.order.OrderCardItemResult;
import com.unimerch.dto.order.OrderChartResult;
import com.unimerch.dto.order.OrderData;

import java.util.Map;

public interface OrderService {
    OrderData saveOrderData(String data, String jwt);

    OrderChartResult getChartAllAcc();

    OrderChartResult getChartUser(Integer id);

    OrderChartResult getChartGroup(Integer id);

    Map<String, OrderCardItemResult> getCardsUser(Integer amznAccId);

    OrderCardItemResult getCardUserAllTime(Integer amznAccId);

    OrderCardItemResult getCardUserToday(Integer amznAccId);

    OrderCardItemResult getCardUserYesterday(Integer amznAccId);

    OrderCardItemResult getCardUserLastSevenDays(Integer amznAccId);

    OrderCardItemResult getCardUserThisMonth(Integer amznAccId);

    OrderCardItemResult getCardUserPreviousMonth(Integer amznAccId);

    Map<String, OrderCardItemResult> getCardsGroup(Integer groupId);

    OrderCardItemResult getCardGroupAllTime(Integer groupId);

    OrderCardItemResult getCardGroupToday(Integer groupIdd);

    OrderCardItemResult getCardGroupYesterday(Integer groupId);

    OrderCardItemResult getCardGroupLastSevenDays(Integer groupId);

    OrderCardItemResult getCardGroupThisMonth(Integer groupId);

    OrderCardItemResult getCardGroupPreviousMonth(Integer groupId);

    Map<String, OrderCardItemResult> getCardAllAcc();

    OrderCardItemResult getCardAllAccAllTime();

    OrderCardItemResult getCardAllAccToday();

    OrderCardItemResult getCardAllAccYesterday();

    OrderCardItemResult getCardAllAccLastSevenDays();

    OrderCardItemResult getCardAllAccThisMonth();

    OrderCardItemResult getCardAllAccPreviousMonth();
}
