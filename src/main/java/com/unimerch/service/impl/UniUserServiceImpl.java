package com.unimerch.service.impl;


import com.unimerch.dto.group.GroupItemResult;
import com.unimerch.dto.user.UserCreateParam;
import com.unimerch.dto.user.UserItemResult;
import com.unimerch.exception.*;
import com.unimerch.mapper.GroupMapper;
import com.unimerch.mapper.UserMapper;
import com.unimerch.repository.BrgGroupUserRepository;
import com.unimerch.repository.UserRepository;
import com.unimerch.repository.datatable.UserDataTableRepository;
import com.unimerch.repository.model.*;
import com.unimerch.security.uni.UniUserPrinciple;
import com.unimerch.service.UniUserService;
import com.unimerch.util.PrincipalUtils;
import com.unimerch.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
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
    private PrincipalUtils principalUtils;

    @Autowired
    private ValidationUtils validationUtils;

    @Autowired
    private RoleServiceImpl roleService;

    @Override
    public DataTablesOutput<UserItemResult> findAllUserDTOExclSelf(DataTablesInput input, String principalUsername) {
        List<Column> columnList = input.getColumns();
        columnList.remove(columnList.size() - 1);
        input.setColumns(columnList);

        return userDataTableRepository.findAll(input, user -> userMapper.toUserItemResult(user));
    }

    @Override
    public UserItemResult findUserItemResultByUsername(String username) {
        return userMapper.toUserItemResult(getByUsername(username));
    }

    @Override
    public User getByUsername(String username) {
        Optional<User> optUser = userRepository.findByUsername(username);
        if (!optUser.isPresent())
            throw new UserNotFoundException(messageSource.getMessage("validation.usernameNotExist", null, Locale.getDefault()));

        return optUser.get();
    }

    @Override
    public User findById(String id) {
        if (!validationUtils.isIdValid(id))
            throw new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault()));

        int validId = Integer.parseInt(id);
        Optional<User> optionalUser = userRepository.findById(validId);
        if (!optionalUser.isPresent()) {
            throw new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault()));
        }

        return optionalUser.get();
    }

    @Override
    public UserItemResult findUserListItemById(String id) {
        return userMapper.toUserItemResult(findById(id));
    }

    @Override
    public UserItemResult create(UserCreateParam userCreateParam) {
        User newUser = userMapper.toUser(userCreateParam);

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

        return userMapper.toUserItemResult(newUser);
    }


    @Override
    public void changePassword(String id, String password) {
        User user = findById(id);

        if (!validationUtils.isPasswordvalid(password))
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
        User user = findById(String.valueOf(principalUtils.getPrincipalId()));

        if (!validationUtils.isPasswordvalid(password))
            throw new InvalidPasswordException(messageSource.getMessage("validation.validPassword", null, Locale.getDefault()));

        try {
            String newPasswordHash = passwordEncoder.encode(password);
            userRepository.changePassword(user.getId(), newPasswordHash);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public UserItemResult changeStatus(String id) {
        User user = findById(id);

        if (roleService.isUserAdmin(id))
            throw new NotAllowDisableException(messageSource.getMessage("error.403", null, Locale.getDefault()));

        user.setDisabled(!user.isDisabled());

        try {
            user = userRepository.save(user);
            return userMapper.toUserItemResult(user);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.500", null, Locale.getDefault()));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent())
            throw new UsernameNotFoundException(username);

        return UniUserPrinciple.build(userOptional.get());
    }

    @Override
    public List<GroupItemResult> findAssignedGroups(String userId) {
        User user = findById(userId);

        List<GroupItemResult> groupListResult = new ArrayList<>();

        brgGroupUserRepository.findAssignedGroupsByUserId(user.getId())
                .forEach(group -> groupListResult.add(groupMapper.toGroupItemResult(group)));

        return groupListResult;
    }

    @Override
    public List<GroupItemResult> findUnassignedGroups(String userId) {
        User user = findById(userId);

        List<GroupItemResult> groupListResult = new ArrayList<>();

        brgGroupUserRepository.findUnassignedGroupsByUserId(user.getId())
                .forEach(group -> groupListResult.add(groupMapper.toGroupItemResult(group)));

        return groupListResult;
    }

    @Override
    public List<GroupItemResult> assignGroupToUser(String userId, List<String> listGroupId) {
        if (listGroupId.isEmpty())
            throw new DataInputException(messageSource.getMessage("validation.inputEmpty", null, Locale.getDefault()));

        User user = findById(userId);
        List<GroupItemResult> groupListResult = new ArrayList<>();

        for (String groupId : listGroupId) {
            Group group = groupService.findById(groupId);

            BrgGroupUserId bridgeId = new BrgGroupUserId(group.getId(), user.getId());
            BrgGroupUser newBridge = new BrgGroupUser(bridgeId, group, user);

            brgGroupUserRepository.save(newBridge);
            groupListResult.add(groupMapper.toGroupItemResult(group));
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
