package com.unimerch.service.impl;

import com.unimerch.dto.group.GroupResult;
import com.unimerch.dto.user.CreateUserParam;
import com.unimerch.dto.user.UserResult;
import com.unimerch.exception.*;
import com.unimerch.mapper.GroupMapper;
import com.unimerch.mapper.UserMapper;
import com.unimerch.repository.datatable.UserDataTableRepository;
import com.unimerch.repository.group.BrgGroupUserRepository;
import com.unimerch.repository.model.group.BrgGroupUser;
import com.unimerch.repository.model.group.BrgGroupUserId;
import com.unimerch.repository.model.group.Group;
import com.unimerch.repository.model.metamodel.User_;
import com.unimerch.repository.model.user.Role;
import com.unimerch.repository.model.user.User;
import com.unimerch.repository.user.UserRepository;
import com.unimerch.service.UniUserService;
import com.unimerch.util.NaturalSortUtils;
import com.unimerch.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UniUserServiceImpl implements UniUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDataTableRepository userDataTableRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GroupServiceImpl groupService;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private BrgGroupUserRepository brgGroupUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RoleServiceImpl roleService;

    private Specification<User> notAdmin() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(User_.ROLE), 1);
    }

    @Override
    public DataTablesOutput<UserResult> findAllUserDTOExclSelf(DataTablesInput input, String principalUsername) {
        List<Column> columnList = input.getColumns();
        columnList.remove(columnList.size() - 1);
        input.setColumns(columnList);
        return userDataTableRepository.findAll(
                input,
                null,
                notAdmin(),
                user -> userMapper.toDTO(user));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        Optional<User> optUser = userRepository.findByUsername(username);
        if (!optUser.isPresent())
            throw new UserNotFoundException(messageSource.getMessage("validation.usernameNotExist", null, Locale.getDefault()));

        return optUser.get();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(String id) {
        if (!ValidationUtils.isIdValid(id))
            throw new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault()));

        int validId = Integer.parseInt(id);
        Optional<User> optionalUser = userRepository.findById(validId);
        if (!optionalUser.isPresent()) {
            throw new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault()));
        }

        return optionalUser.get();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResult findUserListById(String id) {
        return userMapper.toDTO(findById(id));
    }

    @Override
    @Transactional
    public UserResult create(CreateUserParam userCreateParam) {
        //Transiet
        User newUser = userMapper.toModel(userCreateParam);

        newUser.setSalt("abc");
        newUser.setPasswordHash(passwordEncoder.encode(userCreateParam.getPassword()));
        newUser.setRole(new Role(2));

        if (userRepository.existsByUsername(userCreateParam.getUsername()))
            throw new DuplicateDataException(messageSource.getMessage("validation.usernameExists", null, Locale.getDefault()));

        try {
            newUser = userRepository.save(newUser);

        } catch (DataIntegrityViolationException e) {
            throw new DataInputException(messageSource.getMessage("validation.invalidAccountInformation", null, Locale.getDefault()));
        }
        return userMapper.toDTO(newUser);
    }


    @Override
    public void changePassword(String id, String password) {
        User user = findById(id);

        if (!ValidationUtils.isPasswordvalid(password))
            throw new InvalidPasswordException(messageSource.getMessage("validation.validPassword", null, Locale.getDefault()));

        try {
            String newPasswordHash = passwordEncoder.encode(password);
            userRepository.changePassword(user.getId(), newPasswordHash);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public void changeMyPassword(String password) {
        throw new DuplicateDataException("Chức năng tạm khoá");

//        User user = findById(String.valueOf(principalUtils.getPrincipalId()));
//
//        if (!ValidationUtils.isPasswordvalid(password))
//            throw new InvalidPasswordException(messageSource.getMessage("validation.validPassword", null, Locale.getDefault()));
//
//        try {
//            String newPasswordHash = passwordEncoder.encode(password);
//            userRepository.changePassword(user.getId(), newPasswordHash);
//        } catch (Exception e) {
//            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
//        }
    }

    @Override
    public UserResult changeStatus(String id) {
        User user = findById(id);

        if (user.getRole().getId() == 1)
            throw new NotAllowDisableException(messageSource.getMessage("error.403", null, Locale.getDefault()));

        user.setDisabled(!user.isDisabled());

        try {
            user = userRepository.save(user);
            return userMapper.toDTO(user);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public List<GroupResult> findAssignedGroups(String userId) {
        User user = findById(userId);
        return brgGroupUserRepository
                .findAssignedGroupsByUserId(user.getId()).stream()
                .sorted((o1, o2) -> NaturalSortUtils.compareString(o1.getTitle(), o2.getTitle()))
                .map(item -> groupMapper.toGroupResult(item))
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupResult> findUnassignedGroups(String userId) {
        User user = findById(userId);
        return brgGroupUserRepository
                .findUnassignedGroupsByUserId(user.getId()).stream()
                .sorted((o1, o2) -> NaturalSortUtils.compareString(o1.getTitle(), o2.getTitle()))
                .map(item -> groupMapper.toGroupResult(item))
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupResult> assignGroupToUser(String userId, List<String> listGroupId) {
        if (listGroupId.isEmpty())
            throw new DataInputException(messageSource.getMessage("validation.inputEmpty", null, Locale.getDefault()));

        User user = findById(userId);
        List<GroupResult> groupListResult = new ArrayList<>();

        for (String groupId : listGroupId) {
            Group group = groupService.findById(groupId);

            BrgGroupUserId bridgeId = new BrgGroupUserId(group.getId(), user.getId());
            BrgGroupUser newBridge = new BrgGroupUser(bridgeId, group, user);

            brgGroupUserRepository.save(newBridge);
            groupListResult.add(groupMapper.toGroupResult(group));
        }

        return groupListResult;
    }

    @Override
    public void removeGroupFromUser(String userId, String groupId) {
        Group group = groupService.findById(groupId);
        User user = findById(userId);

        brgGroupUserRepository.removeGroupFromUser(group, user);
    }
}
