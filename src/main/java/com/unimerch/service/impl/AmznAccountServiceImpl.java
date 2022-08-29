package com.unimerch.service.impl;

import com.unimerch.dto.amznacc.AmznAccParam;
import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.dto.user.LoginParam;
import com.unimerch.exception.DuplicateDataException;
import com.unimerch.exception.InvalidIdException;
import com.unimerch.exception.ServerErrorException;
import com.unimerch.mapper.AmznAccountMapper;
import com.unimerch.repository.AmznAccountRepository;
import com.unimerch.repository.BrgGroupAmznAccountRepository;
import com.unimerch.repository.OrderRepository;
import com.unimerch.repository.datatable.AmznAccTableRepository;
import com.unimerch.repository.model.AmznAccount;
import com.unimerch.repository.model.Group;
import com.unimerch.service.AmznAccountService;
import com.unimerch.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AmznAccountServiceImpl implements AmznAccountService {

    @Autowired
    private AmznAccTableRepository amznAccTableRepository;

    @Autowired
    private AmznAccountMapper amznAccountMapper;

    @Autowired
    private AmznAccountRepository amznAccountRepository;

    @Autowired
    private BrgGroupAmznAccountRepository brgGroupAmznAccountRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ValidationUtils validationUtils;

    @Override
    public AmznAccount findById(String id) {
        if (!validationUtils.isIdValid(id)) {
            throw new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault()));
        }

        int validId = Integer.parseInt(id);
        Optional<AmznAccount> optionalAmznAcc = amznAccountRepository.findById(validId);
        if (!optionalAmznAcc.isPresent()) {
            throw new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault()));
        }
        return optionalAmznAcc.get();
    }

    @Override
    public DataTablesOutput<AmznAccResult> findAll(DataTablesInput input) {
        try {
            Map<String, Column> columnMap = input.getColumnsAsMap();
            columnMap.remove(null);
            List<Column> columnList = new ArrayList<>(columnMap.values());
            input.setColumns(columnList);
            return amznAccTableRepository.findAll(input, amznAccount -> amznAccountMapper.toAmznAccResult(amznAccount));
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public List<AmznAccResult> findAll() {
        return amznAccountRepository.findAll()
                .stream().map(account -> amznAccountMapper.toAmznAccResult(account))
                .collect(Collectors.toList());
    }

    @Override
    public AmznAccResult create(AmznAccParam amznAccCreateParam) {
        try {
            String username = amznAccCreateParam.getUsername().trim().toLowerCase();
            if (amznAccountRepository.existsByUsername(username)) {
                throw new DuplicateDataException(messageSource.getMessage("validation.amznAccUsernameExists", null, Locale.getDefault()));
            }
            amznAccCreateParam.setUsername(username);
            AmznAccount newAccount = amznAccountRepository.save(amznAccountMapper.toAmznAcc(amznAccCreateParam));
            return amznAccountMapper.toAmznAccResult(newAccount);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public AmznAccResult update(String id, AmznAccParam amznAccParam) {
        AmznAccount amznAccount = findById(id);
        try {
            amznAccount.setPassword(amznAccParam.getPassword());
            amznAccount = amznAccountRepository.save(amznAccount);
            return amznAccountMapper.toAmznAccResult(amznAccount);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public void delete(String id) {
        AmznAccount amznAccount = findById(id);
        try {
            orderRepository.deleteByAmznAccount_Id(amznAccount.getId());
            brgGroupAmznAccountRepository.deleteByAmznAccount_Id(amznAccount.getId());
            amznAccountRepository.delete(amznAccount);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

}
