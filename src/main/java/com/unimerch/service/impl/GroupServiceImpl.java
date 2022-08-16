package com.unimerch.service.impl;

import com.unimerch.dto.AmznAccAddedToGroup;
import com.unimerch.exception.DuplicateDataException;
import com.unimerch.exception.InvalidIdException;
import com.unimerch.exception.NoDataFoundException;
import com.unimerch.exception.ServerErrorException;
import com.unimerch.mapper.AmznAccountMapper;
import com.unimerch.repository.AmznAccountRepository;
import com.unimerch.repository.BrgGroupAmznAccountRepository;
import com.unimerch.repository.GroupRepository;
import com.unimerch.repository.model.*;
import com.unimerch.service.GroupService;
import com.unimerch.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
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

    @Override
    public List<Group> findAll() {
        List<Group> groupList = groupRepository.findAll();
        if (groupList.isEmpty()) {
            throw new NoDataFoundException(messageSource.getMessage("error.noDataFound", null, Locale.getDefault()));
        }
        return groupList;
    }

    @Override
    public Optional<Group> findById(String id) {
        boolean isIdValid = Pattern.matches(ValidationUtils.ID_REGEX, id);
        if (!isIdValid) {
            throw new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault()));
        }

        int validId = Integer.parseInt(id);
        Optional<Group> optionalGroup = groupRepository.findById(validId);
        if (!optionalGroup.isPresent()) {
            throw new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault()));
        }
        return optionalGroup;
    }

    @Override
    public Group createGroup(String groupTitle) {
        if (groupRepository.existsByTitle(groupTitle.trim())) {
            throw new DuplicateDataException(messageSource.getMessage("validation.groupTitleExists", null, Locale.getDefault()));
        }

        try {
            Group newGroup = new Group(groupTitle.trim());
            return groupRepository.save(newGroup);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.serverError", null, Locale.getDefault()));
        }
    }

    @Override
    public Group updateGroup(String id, String groupTitle) {
        Group group = findById(id).get();
        String newGroupTitle = groupTitle.trim();
        boolean isGroupInvalid = groupRepository.existsByTitleAndIdIsNot(newGroupTitle, group.getId());

        if (isGroupInvalid) {
            throw new DuplicateDataException(messageSource.getMessage("validation.groupTitleExists", null, Locale.getDefault()));
        }

        try {
            group.setTitle(newGroupTitle);
            return groupRepository.save(group);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.serverError", null, Locale.getDefault()));
        }
    }

    @Override
    public List<AmznAccAddedToGroup> addAmznAccToGroup(ArrayList<String> amznAccIdList, String id) {

        Group group = findById(id).get();
        List<AmznAccAddedToGroup> amznAccAddedToGroupList = new ArrayList<>();

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
                amznAccAddedToGroupList.add(amznAccountMapper.toAmznAccAddedToGroup(brgGroupAmznAccount));
            }

        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.serverError", null, Locale.getDefault()));
        }

        return amznAccAddedToGroupList;
    }

    @Override
    public List<AmznAccAddedToGroup> getAmznAccInsideGroup(String id) {
        Group group = findById(id).get();
        return brgGroupAmznAccRepo.getAmznAccInGroup(group.getId());
    }

    @Override
    public List<AmznAccAddedToGroup> getAmznAccOutsideGroup(String id) {
        Group group = findById(id).get();
        return brgGroupAmznAccRepo.getAmznAccOutGroup(group.getId());
    }

    @Override
    public void deleteAmznAccFromGroup(int amznAccId, int groupId) {
        brgGroupAmznAccRepo.deleteAmznAccFromGroup(amznAccId, groupId);
    }


}
