package com.unimerch.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.unimerch.dto.amznacc.*;
import com.unimerch.exception.*;
import com.unimerch.mapper.AmznUserMapper;
import com.unimerch.mapper.MetadataMapper;
import com.unimerch.repository.AmznUserRepository;
import com.unimerch.repository.BrgGroupAmznAccountRepository;
import com.unimerch.repository.OrderRepository;
import com.unimerch.repository.datatable.AmznAccTableRepository;
import com.unimerch.repository.model.AmznUser;
import com.unimerch.repository.model.AzmnStatus;
import com.unimerch.repository.model.Group;
import com.unimerch.service.AmznUserService;
import com.unimerch.service.GroupService;
import com.unimerch.util.ValidationUtils;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AmznUserServiceImpl implements AmznUserService {

    @Autowired
    private AmznAccTableRepository amznAccTableRepository;

    @Autowired
    private AmznUserMapper amznMapper;

    @Autowired
    private AmznUserRepository amznAccountRepository;

    @Autowired
    private BrgGroupAmznAccountRepository brgGroupAmznAccountRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MetadataMapper metadataMapper;

    @Autowired
    private ValidationUtils validationUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AmznUser findById(String id) {
        if (!validationUtils.isIdValid(id)) {
            throw new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault()));
        }

        int validId = Integer.parseInt(id);
        Optional<AmznUser> optionalAmznAcc = amznAccountRepository.findById(validId);
        if (!optionalAmznAcc.isPresent()) {
            throw new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault()));
        }
        return optionalAmznAcc.get();
    }

    @Override
    public void updateMetadata(String data, Authentication authentication) {

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Metadata.class, new MetadataMapper());
        mapper.registerModule(module);
        try {
            Metadata metadata = mapper.readValue(data, Metadata.class);
            String username = authentication.getName();
            AmznUser user = amznAccountRepository.findByUsername(username);
            user = metadataMapper.updateAmznAccMetadata(user, metadata);
            amznAccountRepository.save(user);
        } catch (JsonProcessingException | ServerErrorException e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public DataTablesOutput<AmznAccResult> findAll(DataTablesInput input) {
        try {
            Map<String, Column> columnMap = input.getColumnsAsMap();
            columnMap.remove(null);
            List<Column> columnList = new ArrayList<>(columnMap.values());
            input.setColumns(columnList);
            return amznAccTableRepository.findAll(input, amznAccount -> amznMapper.toDTO(amznAccount));
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public List<AmznAccResult> findAll() {
        return amznAccountRepository.findAll().stream().map(amznMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AmznAccFilterItemResult> findAllFilter() {
        return amznAccountRepository.findAll()
                .stream().map(account -> amznMapper.toAmznAccFilterItemResult(account))
                .collect(Collectors.toList());
    }

    @Override
    public AmznAccResult create(AmznAccParam createParam) {
        String username = createParam.getUsername().trim().toLowerCase();
        String password = createParam.getPassword();
        if (amznAccountRepository.existsByUsername(username)) {
            throw new DuplicateDataException(messageSource.getMessage("validation.amznAccUsernameExists", null, Locale.getDefault()));
        }
        try {
            createParam.setUsername(username);
            createParam.setPassword(passwordEncoder.encode(password));
            AmznUser user = amznMapper.toAmznAcc(createParam);
            user.setStatus(AzmnStatus.APPROVED);
            user = amznAccountRepository.save(user);
            return amznMapper.toDTO(user);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public AmznAccResult update(String id, AmznAccParam amznAccParam) {
        AmznUser amznAccount = findById(id);
        try {
            amznAccount.setPassword(passwordEncoder.encode(amznAccParam.getPassword()));
            amznAccount = amznAccountRepository.save(amznAccount);
            return amznMapper.toDTO(amznAccount);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public void delete(String id) {
        AmznUser amznAccount = findById(id);
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

        List<AmznAccResult> amznAccResultList;
        List<AmznUser> amznUserList = new ArrayList<>();
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
//                AmznAccount newAmznAcc = amznAccountRepository.save(amznUserMapper.toAmznAcc(newAmznAccParam));
//                amznAccResultList.add(amznUserMapper.toAmznAccResult(newAmznAcc));
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
                if (cell.getCellType() == CellType.STRING && cell.getRichStringCellValue().getString().trim().equalsIgnoreCase("username")) {
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

            if (amznPassword == null || amznUsername == null) {
                continue;
            }

            amznPassword = passwordEncoder.encode(amznPassword);
            AmznAccParam newAmznAccParam = new AmznAccParam(amznUsername, amznPassword);
            amznUserList.add(amznMapper.toAmznAcc(newAmznAccParam));

        }
        try {
            amznUserList = amznAccountRepository.saveAll(amznUserList);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
        amznAccResultList = amznUserList.stream().map(amznUser -> amznMapper.toDTO(amznUser)).collect(Collectors.toList());
        return amznAccResultList;
    }

    @Override
    public AmznAccResult findByUsername(String username) {
        AmznUser user = amznAccountRepository.findByUsername(username);
        if (user == null)
            throw new UserNotFoundException("{exception.userNotFound}");
        return amznMapper.toDTO(user);
    }

    @Override
    public List<AmznAccAnalyticsItemResult> findAllAnalytics() {
        return amznAccountRepository.findAll().stream()
                .map(amznUser -> amznMapper.toAmznAccAnalyticsItemResult(amznUser))
                .collect(Collectors.toList());
    }

    @Override
    public List<AmznAccAnalyticsItemResult> findAnalyticsByGrpId(String groupId) {
        Group group = groupService.findById(groupId);
        return brgGroupAmznAccountRepository.getAmznAccInGroup(group.getId()).stream()
                .map(amznAccResult -> amznMapper.toAmznAccAnalyticsItemResult(amznAccResult))
                .collect(Collectors.toList());
    }

    @Override
    public List<AmznAccAnalyticsItemResult> findAnalyticsByAmznAccId(String amznAccId) {
        List<AmznAccAnalyticsItemResult> analyticsList = new ArrayList<>();
        analyticsList.add(amznMapper.toAmznAccAnalyticsItemResult(findById(amznAccId)));
        return analyticsList;
    }
}
