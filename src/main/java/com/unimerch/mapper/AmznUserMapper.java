package com.unimerch.mapper;

import com.unimerch.dto.amznacc.AmznAccAnalyticsItemResult;
import com.unimerch.dto.amznacc.AmznAccFilterItemResult;
import com.unimerch.dto.amznacc.AmznAccParam;
import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.repository.model.AmznUser;
import com.unimerch.repository.model.AzmnStatus;
import com.unimerch.repository.model.BrgGroupAmznAccount;
import com.unimerch.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AmznUserMapper {
    @Autowired
    OrderService orderService;

    public AmznAccResult toDTO(BrgGroupAmznAccount brgGroupAmznAccount) {
        return new AmznAccResult()
                .setId(brgGroupAmznAccount.getAmznAccount().getId())
                .setUsername((brgGroupAmznAccount.getAmznAccount().getUsername()));
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

    public AmznAccFilterItemResult toAmznAccFilterItemResult(AmznUser amznUser) {
        int soldToday = orderService.getNumberSoldInDayByAmznId(amznUser.getId());

        return new AmznAccFilterItemResult()
                .setId(amznUser.getId())
                .setUsername(amznUser.getUsername())
                .setSoldToday(soldToday);
    }

    public AmznAccAnalyticsItemResult toAmznAccAnalyticsItemResult(AmznUser amznUser) {
        int slotRemaining = amznUser.getDailyProductLimit() - amznUser.getDailyProductCount();

        return new AmznAccAnalyticsItemResult()
                .setId(amznUser.getId())
                .setUsername(amznUser.getUsername())
                .setPublished(null)
                .setTier(amznUser.getTier())
                .setSlotRemaining(slotRemaining)
                .setSlotTotal(amznUser.getDailyProductLimit())
                .setReject(amznUser.getTotalRejected())
                .setRemove(amznUser.getTotalRemoved())
                .setNote(amznUser.getNote());
    }


}
