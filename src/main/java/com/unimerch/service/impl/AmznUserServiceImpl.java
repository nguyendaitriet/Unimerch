package com.unimerch.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.unimerch.dto.amznacc.*;
import com.unimerch.exception.*;
import com.unimerch.mapper.AmznUserMapper;
import com.unimerch.mapper.MetadataMapper;
import com.unimerch.repository.AmznUserRepository;
import com.unimerch.repository.BrgGroupAmznUserRepository;
import com.unimerch.repository.OrderRepository;
import com.unimerch.repository.datatable.AmznAccTableRepository;
import com.unimerch.repository.model.AmznUser;
import com.unimerch.repository.model.AzmnStatus;
import com.unimerch.repository.model.Group;
import com.unimerch.service.AmznUserService;
import com.unimerch.service.GroupService;
import com.unimerch.util.NaturalSortUtils;
import com.unimerch.util.TimeUtils;
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
import java.time.Instant;
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
    private BrgGroupAmznUserRepository brgGroupAmznUserRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MetadataMapper metadataMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AmznUser findById(String id) {
        if (!ValidationUtils.isIdValid(id)) {
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
    public void updateStatus(String status, Authentication authentication) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(AmznStatus.class, new AmznUserMapper());
        mapper.registerModule(module);
        try {
            AmznStatus amznStatus = mapper.readValue(status, AmznStatus.class);
            String username = authentication.getName();
            AmznUser user = amznAccountRepository.findByUsername(username);
            user.setStatus(AzmnStatus.parseAzmnStatus(amznStatus.getStatus()));
            user.setLastCheck(Instant.now());
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
        return amznAccountRepository.findAll().stream()
                .sorted((s1, s2) -> NaturalSortUtils.compareString(s1.getUsername(), s2.getUsername()))
                .map(amznMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AmznAccFilterResult> findAllFilter() {
        return amznAccountRepository.findAll()
                .stream()
                .sorted((s1, s2) -> NaturalSortUtils.compareString(s1.getUsername(), s2.getUsername()))
                .map(account -> amznMapper.toAmznAccFilterResult(account))
                .collect(Collectors.toList());
    }

    @Override
    public AmznAccResult create(AmznAccParam createParam) {
        String username = createParam.getUsername().trim().toLowerCase();
        String password = createParam.getPassword();
        if (amznAccountRepository.existsByUsername(username)) {
            throw new DuplicateDataException(messageSource.getMessage("validation.usernameExists", null, Locale.getDefault()));
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
            orderRepository.deleteByAmznUserId(amznAccount.getId());
            brgGroupAmznUserRepository.deleteByAmznUserId(amznAccount.getId());
            amznAccountRepository.delete(amznAccount);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public void deleteAllByListId(List<Integer> idList) {
        try {
            orderRepository.deleteAllByAmznUserIdIn(idList);
            brgGroupAmznUserRepository.deleteAllByAmznUserIdIn(idList);
            amznAccountRepository.deleteAllByIdIn(idList);
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
    public List<AmznAccAnalyticsResult> findAllAnalytics() {
        return amznAccountRepository.findAll().stream()
                .map(amznUser -> amznMapper.toAmznAccAnalyticsResult(amznUser))
                .collect(Collectors.toList());
    }

    @Override
    public List<AmznAccAnalyticsResult> findAnalyticsByGrpId(String groupId) {
        Group group = groupService.findById(groupId);
        return brgGroupAmznUserRepository.getAmznAccInGroup(group.getId()).stream()
                .map(amznAccResult -> amznMapper.toAmznAccAnalyticsResult(amznAccResult))
                .collect(Collectors.toList());
    }

    @Override
    public List<AmznAccAnalyticsResult> findAnalyticsByAmznAccId(String amznAccId) {
        List<AmznAccAnalyticsResult> analyticsList = new ArrayList<>();
        analyticsList.add(amznMapper.toAmznAccAnalyticsResult(findById(amznAccId)));
        return analyticsList;
    }

    @Override
    public void addNoteToAmznAcc(String id, String note) {
        AmznUser user = findById(id);

        try {
            user.setNote(note);
            amznAccountRepository.save(user);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public List<AmznAccDieResult> findAllAccDie() {
        List<AmznUser> userList = amznAccountRepository.findByStatus(AzmnStatus.TERMINATED);
        return amznMapper.toAmznAccDieResults(userList);
    }

    @Override
    public List<AmznAccDieResult> findAccDieByGrpId(String groupId) {
        Group group = groupService.findById(groupId);
        List<AmznUser> userList = brgGroupAmznUserRepository.getAmznAccDieInGroup(group.getId());
        return amznMapper.toAmznAccDieResults(userList);
    }

    @Override
    public List<AmznAccLastCheckResult> findAllLastCheck12Hours() {
        Instant twelveHoursBefore = TimeUtils.getInstantSomeHourBefore(12);
        List<AmznUser> userList = amznAccountRepository.findAllByLastCheckBefore(twelveHoursBefore);
        return amznMapper.toAmznLastCheckResult(userList);
    }

    @Override
    public List<AmznAccLastCheckResult> findAllLastCheck12HoursByGrpId(String groupId) {
        Group group = groupService.findById(groupId);
        Instant twelveHoursBefore = TimeUtils.getInstantSomeHourBefore(12);
        List<AmznUser> userList = brgGroupAmznUserRepository.findAllByLastCheckBeforeAndGroupId(twelveHoursBefore, group.getId());
        return amznMapper.toAmznLastCheckResult(userList);
    }
}