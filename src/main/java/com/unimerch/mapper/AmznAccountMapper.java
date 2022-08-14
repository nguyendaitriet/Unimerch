package com.unimerch.mapper;

import com.unimerch.dto.AmznAccAddedToGroup;
import com.unimerch.repository.model.BrgGroupAmznAccount;
import org.springframework.stereotype.Component;

@Component
public class AmznAccountMapper {

    public AmznAccAddedToGroup toAmznAccAddedToGroup (BrgGroupAmznAccount brgGroupAmznAccount) {
        return new AmznAccAddedToGroup()
                .setId(brgGroupAmznAccount.getAmznAccount().getId())
                .setUsername((brgGroupAmznAccount.getAmznAccount().getUserName()));
    }
}
