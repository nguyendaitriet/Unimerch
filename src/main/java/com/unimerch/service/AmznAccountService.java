package com.unimerch.service;

import com.unimerch.dto.amznacc.AmznAccParam;
import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.dto.group.GroupItemResult;
import com.unimerch.dto.user.LoginParam;
import com.unimerch.repository.model.AmznAccount;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface AmznAccountService {

    DataTablesOutput<AmznAccResult> findAll(DataTablesInput input);

    AmznAccount findById(String id);

    AmznAccResult create(AmznAccParam amznAccCreateParam);

    AmznAccResult update(String id, AmznAccParam amznAccParam);

}
