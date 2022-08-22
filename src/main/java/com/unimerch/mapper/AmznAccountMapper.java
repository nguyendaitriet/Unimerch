package com.unimerch.mapper;

import com.unimerch.dto.amznacc.AmznAccAddedToGroup;
import com.unimerch.repository.model.AmznAccount;
import com.unimerch.repository.model.BrgGroupAmznAccount;
import org.springframework.stereotype.Component;

@Component
public class AmznAccountMapper {

    public AmznAccAddedToGroup toAmznAccAddedToGroup (BrgGroupAmznAccount brgGroupAmznAccount) {
        return new AmznAccAddedToGroup()
                .setId(brgGroupAmznAccount.getAmznAccount().getId())
                .setUsername((brgGroupAmznAccount.getAmznAccount().getUsername()));
    }

    public AmznAccAddedToGroup toAmznAccAddedToGroup (AmznAccount amznAccount) {
        return new AmznAccAddedToGroup()
                .setId(amznAccount.getId())
                .setUsername(amznAccount.getUsername());
    }
}
