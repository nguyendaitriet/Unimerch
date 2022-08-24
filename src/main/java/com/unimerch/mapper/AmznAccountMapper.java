package com.unimerch.mapper;

import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.repository.model.AmznAccount;
import com.unimerch.repository.model.BrgGroupAmznAccount;
import org.springframework.stereotype.Component;

@Component
public class AmznAccountMapper {

    public AmznAccResult toAmznAccResult(BrgGroupAmznAccount brgGroupAmznAccount) {
        return new AmznAccResult()
                .setId(brgGroupAmznAccount.getAmznAccount().getId())
                .setUsername((brgGroupAmznAccount.getAmznAccount().getUsername()));
    }

    public AmznAccResult toAmznAccResult(AmznAccount amznAccount) {
        return new AmznAccResult()
                .setId(amznAccount.getId())
                .setUsername(amznAccount.getUsername());
    }
}
