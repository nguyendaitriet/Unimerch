package com.unimerch.service.impl;

import com.unimerch.dto.AmznAccAddedToGroup;
import com.unimerch.exception.DuplicateDataException;
import com.unimerch.exception.InvalidIdException;
import com.unimerch.exception.NoDataFoundException;
import com.unimerch.exception.ServerErrorException;
import com.unimerch.mapper.AmznAccountMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.SpecificationBuilder;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
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
    EntityManager entityManager;

    @Override
    public List<Group> findAll() {
        List<Group> groupList = groupRepository.findAll();
        if (groupList.isEmpty()) {
            throw new NoDataFoundException(messageSource.getMessage("error.noDataFound", null, Locale.getDefault()));
        }
        return groupList;
    }

    @Override
    public DataTablesOutput<Group> findAll(DataTablesInput input) {
        Map<String, Column> columnMap = input.getColumnsAsMap();
        columnMap.remove(null);
        List<Column> columnList = new ArrayList<>(columnMap.values());
        input.setColumns(columnList);
        return groupDataTableRepository.findAll(input);
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

//    @Override
//    public DataTablesOutput<AmznAccAddedToGroup>getAmznAccInsideGroup(String id, DataTablesInput input) {
//        Group group = findById(id).get();
//
//        Map<String, Column> columnMap = input.getColumnsAsMap();
//        columnMap.remove(null);
//        List<Column> columnList = new ArrayList<>(columnMap.values());
//        input.setColumns(columnList);
//
//        List<Integer> amznAccIdInGroupList = brgGroupAmznAccRepo.getAmznAccIdInGroup(group.getId());
//
//
////        Specification<AmznAccount> amznAccountSpecification = (Specification<AmznAccount>) (root, query, criteriaBuilder) -> {
////            CriteriaQuery<AmznAccount> q = criteriaBuilder.createQuery(AmznAccount.class);
//////            q.select(root);
////
////            Expression<Integer> idExpression = root.get("id");
////            Predicate idPredicate = idExpression.in(amznAccIdInGroupList);
//////            q.where(idPredicate);
////            return (Predicate) criteriaBuilder.createQuery(AmznAccount.class).select(root).where(idPredicate);
////        };
//
//        DataTablesOutput<AmznAccAddedToGroup> dataTablesOutput = amznAccTableRepository
//                .findAll(input, amznAccount -> amznAccountMapper.toAmznAccAddedToGroup(amznAccount));
//
//        List<AmznAccAddedToGroup> amznAccAddedToGroupList = dataTablesOutput.getData();
////
////        List<AmznAccAddedToGroup> newAmznAccAddedToGroupList = new ArrayList<>();
////        for (AmznAccAddedToGroup amznAccAddedToGroup : amznAccAddedToGroupList) {
////            int amznAccId = amznAccAddedToGroup.getId();
////            if (amznAccIdInGroupList.contains(amznAccId)){
////                newAmznAccAddedToGroupList.add(amznAccAddedToGroup);
////            }
////        }
//
////        dataTablesOutput.setData(newAmznAccAddedToGroupList);
//        dataTablesOutput.setData(amznAccAddedToGroupList);
//        return dataTablesOutput;
//    }

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
