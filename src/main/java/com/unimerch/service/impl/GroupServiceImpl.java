package com.unimerch.service.impl;

import com.unimerch.dto.amznacc.AmznAccFilterResult;
import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.dto.group.GroupCreateParam;
import com.unimerch.dto.group.GroupResult;
import com.unimerch.dto.group.GroupUpdateParam;
import com.unimerch.exception.DuplicateDataException;
import com.unimerch.exception.InvalidIdException;
import com.unimerch.exception.ServerErrorException;
import com.unimerch.mapper.AmznUserMapper;
import com.unimerch.mapper.GroupMapper;
import com.unimerch.repository.amzn.AmznUserRepository;
import com.unimerch.repository.group.BrgGroupAmznUserRepository;
import com.unimerch.repository.datatable.GroupDataTableRepository;
import com.unimerch.repository.group.GroupRepository;
import com.unimerch.repository.model.*;
import com.unimerch.service.GroupService;
import com.unimerch.util.NaturalSortUtils;
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
public class GroupServiceImpl implements GroupService {

    @Autowired
    private AmznUserRepository amznAccountRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BrgGroupAmznUserRepository brgGroupAmznAccRepo;

    @Autowired
    private AmznUserMapper amznUserMapper;

    @Autowired
    private GroupDataTableRepository groupDataTableRepository;

    @Autowired
    private GroupMapper groupMapper;

    @Override
    public List<GroupResult> findAll() {
        List<GroupResult> groupResultList = groupRepository.findAll()
                .stream().map(group -> groupMapper.toGroupResult(group))
                .sorted((o1, o2) -> NaturalSortUtils.compareString(o1.getTitle(), o2.getTitle()))
                .collect(Collectors.toList());
        return groupResultList;
    }

    @Override
    public DataTablesOutput<GroupResult> findAll(DataTablesInput input) {
        try {
            Map<String, Column> columnMap = input.getColumnsAsMap();
            columnMap.remove(null);
            List<Column> columnList = new ArrayList<>(columnMap.values());
            input.setColumns(columnList);

            return groupDataTableRepository.findAll(input, group -> groupMapper.toGroupResult(group));
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public Group findById(String id) {
        if (!ValidationUtils.isIdValid(id)) {
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
    public GroupResult findGroupItemResultById(String id) {
        return groupMapper.toGroupResult(findById(id));
    }

    @Override
    public GroupResult createGroup(GroupCreateParam groupCreateParam) {
        String groupTitle = groupCreateParam.getTitle().trim();

        if (groupRepository.existsByTitle(groupTitle)) {
            throw new DuplicateDataException(messageSource.getMessage("validation.groupTitleExists", null, Locale.getDefault()));
        }

        try {
            Group newGroup = new Group(groupTitle);
            newGroup = groupRepository.save(newGroup);
            return groupMapper.toGroupResult(newGroup);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    public GroupResult updateGroup(String id, GroupUpdateParam groupUpdateParam) {
        Group group = findById(id);

        String newGroupTitle = groupUpdateParam.getTitle().trim();
        boolean isGroupInvalid = groupRepository.existsByTitleAndIdIsNot(newGroupTitle, group.getId());

        if (isGroupInvalid) {
            throw new DuplicateDataException(messageSource.getMessage("validation.groupTitleExists", null, Locale.getDefault()));
        }

        try {
            group.setTitle(newGroupTitle);
            group = groupRepository.save(group);
            return groupMapper.toGroupResult(group);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
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
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
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
                AmznUser amznAccount = amznAccountRepository.findById(amznAccId).get();
                BrgGroupAmznUserId brgGroupAmznUserId = new BrgGroupAmznUserId(groupId, amznAccId);
                BrgGroupAmznUser brgGroupAmznUser = new BrgGroupAmznUser();

                brgGroupAmznUser.setId(brgGroupAmznUserId);
                brgGroupAmznUser.setGroup(group);
                brgGroupAmznUser.setAmznUser(amznAccount);

                brgGroupAmznAccRepo.save(brgGroupAmznUser);
                amznAccResultList.add(amznUserMapper.toDTO(brgGroupAmznUser));
            }
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }

        return amznAccResultList;
    }

    @Override
    public List<AmznAccResult> getAmznAccInsideGroup(String id) {
        try {
            Group group = findById(id);
            List<AmznUser> amznAccResult = brgGroupAmznAccRepo.getAmznAccInGroup(group.getId());
            return amznAccResult.stream()
                    .sorted((s1, s2) -> NaturalSortUtils.compareString(s1.getUsername(), s2.getUsername()))
                    .map(item -> amznUserMapper.toDTO(item)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public List<AmznAccResult> getAmznAccOutsideGroup(String id) {
        try {
            Group group = findById(id);
            List<AmznUser> amznAccResult = brgGroupAmznAccRepo.getAmznAccOutGroup(group.getId());
            return amznAccResult.stream()
                    .sorted((s1, s2) -> NaturalSortUtils.compareString(s1.getUsername(), s2.getUsername()))
                    .map(item -> amznUserMapper.toDTO(item)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public void deleteAmznAccFromGroup(int amznAccId, int groupId) {
        try {
            brgGroupAmznAccRepo.deleteAmznAccFromGroup(amznAccId, groupId);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public List<AmznAccFilterResult> findAllAmznAccInGrpFilter(Integer groupId) {
        return brgGroupAmznAccRepo.getAmznAccInGroup(groupId)
                .stream()
                .sorted((s1, s2) -> NaturalSortUtils.compareString(s1.getUsername(), s2.getUsername()))
                .map(amznAccount -> amznUserMapper.toAmznAccFilterResult(amznAccount))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMultiAmznAccFromGroup(List<Integer> amznAccSelected, Integer id) {
        try {
            brgGroupAmznAccRepo.deleteAllByGroupIdAndAmznUserIdIn(id, amznAccSelected);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }
}
