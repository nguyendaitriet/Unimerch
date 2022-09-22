package com.unimerch.mapper;

import com.unimerch.dto.amznacc.*;
import com.unimerch.repository.model.AmznUser;
import com.unimerch.repository.model.AzmnStatus;
import com.unimerch.repository.model.BrgGroupAmznUser;
import com.unimerch.service.AmznUserService;
import com.unimerch.service.OrderService;
import com.unimerch.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class AmznUserMapper{
    @Autowired
    OrderService orderService;

    @Autowired
    AmznUserService amznUserService;

    public AmznAccResult toDTO(BrgGroupAmznUser brgGroupAmznUser) {
        return new AmznAccResult()
                .setId(brgGroupAmznUser.getAmznUser().getId())
                .setUsername((brgGroupAmznUser.getAmznUser().getUsername()));
    }

    public AmznAccResult toDTO(AmznUser amznUser) {
        return new AmznAccResult()
                .setId(amznUser.getId())
                .setUsername(amznUser.getUsername());
    }

    public AmznUser toAmznAcc(AmznAccParam amznAccCreateParam) {
        return new AmznUser()
                .setUsername(amznAccCreateParam.getUsername())
                .setPassword(amznAccCreateParam.getPassword())
                .setLastCheck(Instant.now())
                .setDailyProductCount(0)
                .setDailyProductLimit(0)
                .setOverallDesignCount(0)
                .setOverallDesignLimit(0)
                .setOverallProductCount(0)
                .setOverallProductLimit(0)
                .setStatus(AzmnStatus.APPROVED)
                .setTier(0)
                .setTotalRejected(0)
                .setTotalRemoved(0);
    }

    public AmznAccFilterResult toAmznAccFilterResult(AmznUser amznUser) {
        int soldToday = orderService.getNumberSoldInDayByAmznId(amznUser.getId());

        return new AmznAccFilterResult()
                .setId(amznUser.getId())
                .setUsername(amznUser.getUsername())
                .setSoldToday(soldToday);
    }

    public AmznAccAnalyticsResult toAmznAccAnalyticsResult(AmznUser amznUser) {
        int slotRemaining = amznUser.getDailyProductLimit() - amznUser.getDailyProductCount();

        return new AmznAccAnalyticsResult()
                .setId(amznUser.getId())
                .setUsername(amznUser.getUsername())
                .setPublished(amznUser.getOverallDesignCount())
                .setTier(amznUser.getTier())
                .setSlotRemaining(slotRemaining)
                .setSlotTotal(amznUser.getDailyProductLimit())
                .setReject(amznUser.getTotalRejected())
                .setRemove(amznUser.getTotalRemoved())
                .setNote(amznUser.getNote());
    }

    public List<AmznAccDieResult> toAmznAccDieResults(List<AmznUser> amznUsers) {
        List<AmznAccDieResult> accDieList = new ArrayList<>();
        int no = 1;

        for (AmznUser user : amznUsers) {
            String lastCheck = TimeUtils.toDayMonthYear(TimeUtils.instantToLocalDate(user.getLastCheck()));

            accDieList.add(new AmznAccDieResult()
                    .setNo(no++)
                    .setId(user.getId())
                    .setUsername(user.getUsername())
                    .setLastUpdate(lastCheck)
                    .setAccountStatus(user.getStatus().getValue()));
        }

        return accDieList;
    }
}
