package com.unimerch.service.impl;

import com.unimerch.dto.amznacc.AmznAccParam;
import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.dto.user.LoginParam;
import com.unimerch.exception.DuplicateDataException;
import com.unimerch.mapper.AmznAccountMapper;
import com.unimerch.repository.AmznAccountRepository;
import com.unimerch.repository.datatable.AmznAccTableRepository;
import com.unimerch.repository.model.AmznAccount;
import com.unimerch.service.AmznAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class AmznAccountServiceImpl implements AmznAccountService {

    @Autowired
    private AmznAccTableRepository amznAccTableRepository;

    @Autowired
    private AmznAccountMapper amznAccountMapper;

    @Autowired
    private AmznAccountRepository amznAccountRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public DataTablesOutput<AmznAccResult> findAll(DataTablesInput input) {
        Map<String, Column> columnMap = input.getColumnsAsMap();
        columnMap.remove(null);
        List<Column> columnList = new ArrayList<>(columnMap.values());
        input.setColumns(columnList);
        return amznAccTableRepository.findAll(input, amznAccount -> amznAccountMapper.toAmznAccResult(amznAccount));
    }

    @Override
    public AmznAccResult create(AmznAccParam amznAccCreateParam) {
        String username = amznAccCreateParam.getUsername().trim().toLowerCase();
        if (amznAccountRepository.existsByUsername(username)) {
            throw new DuplicateDataException(messageSource.getMessage("validation.amznAccUsernameExists", null, Locale.getDefault()));
        }
        amznAccCreateParam.setUsername(username);
        AmznAccount newAccount = amznAccountRepository.save(amznAccountMapper.toAmznAcc(amznAccCreateParam));
        return amznAccountMapper.toAmznAccResult(newAccount);
    }
}
