package com.unimerch.service;

import com.unimerch.dto.order.OrderCardResult;
import com.unimerch.dto.order.OrderChartResult;
import com.unimerch.dto.order.OrderData;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface OrderService {
    OrderData saveOrderData(String data, Authentication authentication);

    int getNumberSoldInDayByAmznId(Integer amznAccId);

    OrderChartResult getChartAllAcc();

    OrderChartResult getChartUser(Integer id);

    OrderChartResult getChartGroup(Integer id);

    Map<String, OrderCardResult> getCardsUser(Integer amznAccId);

    OrderCardResult getCardUserAllTime(Integer amznAccId);

    OrderCardResult getCardUserToday(Integer amznAccId);

    OrderCardResult getCardUserYesterday(Integer amznAccId);

    OrderCardResult getCardUserLastSevenDays(Integer amznAccId);

    OrderCardResult getCardUserThisMonth(Integer amznAccId);

    OrderCardResult getCardUserPreviousMonth(Integer amznAccId);

    Map<String, OrderCardResult> getCardsGroup(Integer groupId);

    OrderCardResult getCardGroupAllTime(Integer groupId);

    OrderCardResult getCardGroupToday(Integer groupIdd);

    OrderCardResult getCardGroupYesterday(Integer groupId);

    OrderCardResult getCardGroupLastSevenDays(Integer groupId);

    OrderCardResult getCardGroupThisMonth(Integer groupId);

    OrderCardResult getCardGroupPreviousMonth(Integer groupId);

    Map<String, OrderCardResult> getCardAllAcc();

    OrderCardResult getCardAllAccAllTime();

    OrderCardResult getCardAllAccToday();

    OrderCardResult getCardAllAccYesterday();

    OrderCardResult getCardAllAccLastSevenDays();

    OrderCardResult getCardAllAccThisMonth();

    OrderCardResult getCardAllAccPreviousMonth();
}
