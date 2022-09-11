package com.unimerch.mapper;

import com.unimerch.dto.amznacc.AmznAccFilterItemResult;
import com.unimerch.dto.amznacc.AmznAccParam;
import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.repository.model.AmznUser;
import com.unimerch.repository.model.BrgGroupAmznAccount;
import com.unimerch.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AmznAccountMapper {
    @Autowired
    OrderService orderService;

    public AmznAccResult toAmznAccResult(BrgGroupAmznAccount brgGroupAmznAccount) {
        return new AmznAccResult()
                .setId(brgGroupAmznAccount.getAmznAccount().getId())
                .setUsername((brgGroupAmznAccount.getAmznAccount().getUsername()));
    }

    public AmznAccResult toAmznAccResult(AmznUser amznAccount) {
        return new AmznAccResult()
                .setId(amznAccount.getId())
                .setUsername(amznAccount.getUsername());
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
                .setTier(0)
                .setTotalRejected(0)
                .setTotalRemoved(0);
    }

    public AmznAccFilterItemResult toAmznAccFilterItemResult(AmznUser amznAccount) {
        int soldToday = orderService.getNumberSoldInDayByAmznId(amznAccount.getId());

        return new AmznAccFilterItemResult()
                .setId(amznAccount.getId())
                .setUsername(amznAccount.getUsername())
                .setSoldToday(soldToday);
    }
}
