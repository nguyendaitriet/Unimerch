package com.unimerch.service.impl;

import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.dto.group.GroupCreateParam;
import com.unimerch.dto.group.GroupItemResult;
import com.unimerch.dto.group.GroupUpdateParam;
import com.unimerch.exception.DuplicateDataException;
import com.unimerch.exception.InvalidIdException;
import com.unimerch.exception.ServerErrorException;
import com.unimerch.mapper.AmznAccountMapper;
import com.unimerch.mapper.GroupMapper;
import com.unimerch.repository.AmznAccountRepository;
import com.unimerch.repository.BrgGroupAmznAccountRepository;
import com.unimerch.repository.datatable.AmznAccTableRepository;
import com.unimerch.repository.datatable.GroupDataTableRepository;
import com.unimerch.repository.GroupRepository;
import com.unimerch.repository.model.*;
import com.unimerch.service.GroupService;
import com.unimerch.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    @Autowired
    private AmznAccountRepository amznAccountRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BrgGroupAmznAccountRepository brgGroupAmznAccRepo;

    @Autowired
    private AmznAccountMapper amznAccountMapper;

    @Autowired
    private GroupDataTableRepository groupDataTableRepository;

    @Autowired
    private AmznAccTableRepository amznAccTableRepository;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private ValidationUtils validationUtils;

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public DataTablesOutput<GroupItemResult> findAll(DataTablesInput input) {
        Map<String, Column> columnMap = input.getColumnsAsMap();
        columnMap.remove(null);
        List<Column> columnList = new ArrayList<>(columnMap.values());
        input.setColumns(columnList);
        return groupDataTableRepository.findAll(input, group -> groupMapper.toGroupItemResult(group));
    }

    @Override
    public Group findById(String id) {
        if (!validationUtils.isIdValid(id)) {
            throw new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault()));
        }

        int validId = Integer.parseInt(id);
        Optional<Group> optionalGroup = groupRepository.findById(validId);
        if (!optionalGroup.isPresent()) {
            throw new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault()));
        }
        return optionalGroup.get();
    }

    @Override
    public GroupItemResult createGroup(GroupCreateParam groupCreateParam) {
        String groupTitle = groupCreateParam.getTitle().trim();

        if (groupRepository.existsByTitle(groupTitle)) {
            throw new DuplicateDataException(messageSource.getMessage("validation.groupTitleExists", null, Locale.getDefault()));
        }

        try {
            Group newGroup = new Group(groupTitle);
            newGroup = groupRepository.save(newGroup);
            return groupMapper.toGroupItemResult(newGroup);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.serverError", null, Locale.getDefault()));
        }
    }

    public GroupItemResult updateGroup(String id, GroupUpdateParam groupUpdateParam) {
        Group group = findById(id);

        String newGroupTitle = groupUpdateParam.getTitle().trim();
        boolean isGroupInvalid = groupRepository.existsByTitleAndIdIsNot(newGroupTitle, group.getId());

        if (isGroupInvalid) {
            throw new DuplicateDataException(messageSource.getMessage("validation.groupTitleExists", null, Locale.getDefault()));
        }

        try {
            group.setTitle(newGroupTitle);
            group = groupRepository.save(group);
            return groupMapper.toGroupItemResult(group);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.serverError", null, Locale.getDefault()));
        }
    }

    @Override
    public void deleteGroup(String id) {
        Group group = findById(id);
        int groupId = group.getId();

        try {
            groupRepository.deleteGroupAssociateAmznAcc(groupId);
            groupRepository.deleteGroupAssociateUser(groupId);
            groupRepository.deleteGroup(groupId);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.serverError", null, Locale.getDefault()));
        }

        }

    @Override
    public List<AmznAccResult> addAmznAccToGroup(ArrayList<String> amznAccIdList, String id) {

        Group group = findById(id);
        List<AmznAccResult> amznAccResultList = new ArrayList<>();

        try {
            int groupId = group.getId();
            for (String amznAccIdString : amznAccIdList) {
                int amznAccId = Integer.parseInt(amznAccIdString);
                AmznAccount amznAccount = amznAccountRepository.findById(amznAccId).get();
                BrgGroupAmznAccountId brgGroupAmznAccountId = new BrgGroupAmznAccountId(groupId, amznAccId);
                BrgGroupAmznAccount brgGroupAmznAccount = new BrgGroupAmznAccount();

                brgGroupAmznAccount.setId(brgGroupAmznAccountId);
                brgGroupAmznAccount.setGroup(group);
                brgGroupAmznAccount.setAmznAccount(amznAccount);

                brgGroupAmznAccRepo.save(brgGroupAmznAccount);
                amznAccResultList.add(amznAccountMapper.toAmznAccResult(brgGroupAmznAccount));
            }
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.serverError", null, Locale.getDefault()));
        }

        return amznAccResultList;
    }

    @Override
    public List<AmznAccResult> getAmznAccInsideGroup(String id) {
        Group group = findById(id);
        List<AmznAccResult> amznAccResultList = new ArrayList<>();
        List<AmznAccount> amznAccResult =  brgGroupAmznAccRepo.getAmznAccInGroup(group.getId());
        amznAccResult.forEach((result) -> amznAccResultList.add(amznAccountMapper.toAmznAccResult(result)));
        return amznAccResultList;    }

    @Override
    public List<AmznAccResult> getAmznAccOutsideGroup(String id) {
        Group group = findById(id);
        List<AmznAccResult> amznAccResultList = new ArrayList<>();
        List<AmznAccount> amznAccResult =  brgGroupAmznAccRepo.getAmznAccOutGroup(group.getId());
        amznAccResult.forEach((result) -> amznAccResultList.add(amznAccountMapper.toAmznAccResult(result)));
        return amznAccResultList;
    }

    @Override
    public void deleteAmznAccFromGroup(int amznAccId, int groupId) {
        brgGroupAmznAccRepo.deleteAmznAccFromGroup(amznAccId, groupId);
    }

}
