package com.unimerch.service.impl;

import com.unimerch.dto.amznacc.AmznAccFilterItemResult;
import com.unimerch.dto.amznacc.AmznAccParam;
import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.dto.user.LoginParam;
import com.unimerch.exception.DuplicateDataException;
import com.unimerch.exception.InvalidFileFormat;
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
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public List<AmznAccFilterItemResult> findAllFilter() {
        return amznAccountRepository.findAll()
                .stream().map(account -> amznAccountMapper.toAmznAccFilterItemResult(account))
                .collect(Collectors.toList());
    }

    @Override
    public AmznAccResult create(AmznAccParam amznAccCreateParam) {
        String username = amznAccCreateParam.getUsername().trim().toLowerCase();
        String password = amznAccCreateParam.getPassword();
        if (amznAccountRepository.existsByUsername(username)) {
            throw new DuplicateDataException(messageSource.getMessage("validation.amznAccUsernameExists", null, Locale.getDefault()));
        }
        try {
            amznAccCreateParam.setUsername(username);
            amznAccCreateParam.setPassword(passwordEncoder.encode(password));
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
            amznAccount.setPassword(passwordEncoder.encode(amznAccParam.getPassword()));
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

    @Override
    public List<AmznAccResult> importFile(MultipartFile amznAccFile) {

        List<AmznAccResult> amznAccResultList = new ArrayList<>();
        Workbook workbook;
        Sheet sheet;
        try {
            workbook = new XSSFWorkbook(amznAccFile.getInputStream());
        } catch (IOException | NotOfficeXmlFileException e) {
            throw new InvalidFileFormat(messageSource.getMessage("validation.invalidFileFormat", null, Locale.getDefault()));
        }

//        for (Row row : sheet) {
//            String amznUsername = null;
//            String amznPassword = null;
//
//            boolean isUsernameExisted = false;
//            for (Cell cell : row) {
//                if (cell.getRowIndex() == 0) {
//                    break;
//                }
//                if (cell.getColumnIndex() == 0) {
//                    switch (cell.getCellType()) {
//                        case STRING:
//                            amznUsername = cell.getRichStringCellValue().getString().trim().toLowerCase();
//                            break;
//                        case NUMERIC:
//                            amznUsername = String.valueOf((int) cell.getNumericCellValue());
//                            break;
//                    }
//                }
//
//                if (amznAccountRepository.existsByUsername(amznUsername)) {
//                    isUsernameExisted = true;
//                    break;
//                }
//
//                if (cell.getColumnIndex() == 1) {
//                    switch (cell.getCellType()) {
//                        case STRING:
//                            amznPassword = cell.getRichStringCellValue().getString().trim().toLowerCase();
//                            break;
//                        case NUMERIC:
//                            amznPassword = String.valueOf((int) cell.getNumericCellValue());
//                            break;
//                    }
//                }
//            }
//
//            if (row.getRowNum() == 0 || isUsernameExisted) {
//                continue;
//            }
//
//            try {
//                AmznAccParam newAmznAccParam = new AmznAccParam(amznUsername, amznPassword);
//                AmznAccount newAmznAcc = amznAccountRepository.save(amznAccountMapper.toAmznAcc(newAmznAccParam));
//                amznAccResultList.add(amznAccountMapper.toAmznAccResult(newAmznAcc));
//            } catch (Exception e) {
//                throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
//            }
//        }

        sheet = workbook.getSheetAt(0);
        int usernameColumnIndex = 0;
        int usernameRowIndex = 0;
        for (Row row : sheet) {
            String amznUsername = null;
            String amznPassword = null;
            boolean isUsernameExisted = false;
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING &&
                        cell.getRichStringCellValue().getString().trim().equalsIgnoreCase("username")) {
                    usernameColumnIndex = cell.getColumnIndex();
                    usernameRowIndex = cell.getRowIndex();
                    break;
                }

                if (cell.getColumnIndex() == usernameColumnIndex) {
                    switch (cell.getCellType()) {
                        case STRING:
                            amznUsername = cell.getRichStringCellValue().getString().trim().toLowerCase();
                            break;
                        case NUMERIC:
                            amznUsername = String.valueOf((int) cell.getNumericCellValue());
                            break;
                    }
                }

                if (amznAccountRepository.existsByUsername(amznUsername)) {
                    isUsernameExisted = true;
                    break;
                }

                if (cell.getColumnIndex() == usernameColumnIndex + 1) {
                    switch (cell.getCellType()) {
                        case STRING:
                            amznPassword = cell.getRichStringCellValue().getString().trim().toLowerCase();
                            break;
                        case NUMERIC:
                            amznPassword = String.valueOf((int) cell.getNumericCellValue());
                            break;
                    }
                }
            }

            if (row.getRowNum() == usernameRowIndex || isUsernameExisted) {
                continue;
            }

            try {
                amznPassword = passwordEncoder.encode(amznPassword);
                AmznAccParam newAmznAccParam = new AmznAccParam(amznUsername, amznPassword);
                AmznAccount newAmznAcc = amznAccountRepository.save(amznAccountMapper.toAmznAcc(newAmznAccParam));
                amznAccResultList.add(amznAccountMapper.toAmznAccResult(newAmznAcc));
            } catch (Exception e) {
                throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
            }
        }

        return amznAccResultList;
    }

    @Override
    public void updateMetadata(String data, String jwt) {

    }

}
