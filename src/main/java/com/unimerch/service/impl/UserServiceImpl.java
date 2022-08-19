package com.unimerch.service.impl;


import com.unimerch.dto.UserCreateParam;
import com.unimerch.dto.UserListItem;
import com.unimerch.exception.*;
import com.unimerch.mapper.UserMapper;
import com.unimerch.repository.datatable.GroupDataTableRepository;
import com.unimerch.repository.datatable.UserDataTableRepository;
import com.unimerch.repository.UserRepository;
import com.unimerch.repository.model.Group;
import com.unimerch.repository.model.Role;
import com.unimerch.repository.model.User;
import com.unimerch.repository.model.UserPrinciple;
import com.unimerch.repository.specification.UserSpecification;
import com.unimerch.service.UserService;
import com.unimerch.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDataTableRepository userDataTableRepository;

    @Autowired
    private GroupDataTableRepository groupDataTableRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    @Override
    public List<UserListItem> findAllUsersDTOExclSelf(String principalUsername) {
        List<UserListItem> userListItemList = userRepository.findAllUserListItemsExclSelf(principalUsername);
        if (userListItemList.isEmpty()) {
            throw new NoDataFoundException(messageSource.getMessage("error.noDataFound", null, Locale.getDefault()));
        }
        return userListItemList;
    }

    @Override
    public DataTablesOutput<UserListItem> findAllUserDTOExclSelf(DataTablesInput input, String principalUsername) {
        List<Column> columnList = input.getColumns();
        columnList.remove(columnList.size() -1 );
        input.setColumns(columnList);

//        (input, user -> userMapper.toUserListItem(user))
        return userDataTableRepository.findAll(input, user -> userMapper.toUserListItem(user));
    }

    public DataTablesOutput<Group> findAllGrpAssigned(DataTablesInput input, Integer userId) {


//        return groupDataTableRepository.findAll(input, group -> );
        return null;
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(String id) {
        boolean isIdValid = Pattern.matches(ValidationUtils.ID_REGEX, id);
        if (!isIdValid) {
            throw new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault()));
        }

        int validId = Integer.parseInt(id);
        Optional<User> optionalUser = userRepository.findById(validId);
        if (!optionalUser.isPresent()) {
            throw new InvalidIdException(messageSource.getMessage("validation.idNotExist", null, Locale.getDefault()));
        }

        return optionalUser;
    }

    @Override
    public UserListItem findUserListItemById(String id) {
        return userMapper.toUserListItem(findById(id).get());
    }

    @Override
    public UserListItem create(UserCreateParam userCreateParam) {
        User newUser = userMapper.toUser(userCreateParam);

        newUser.setSalt("abc");
        newUser.setPasswordHash(passwordEncoder.encode(userCreateParam.getPassword()));
        newUser.setRole(new Role(2));

        if (userRepository.existsByUsername(userCreateParam.getUsername())) {
            throw new DuplicateDataException(messageSource.getMessage("validation.usernameExists", null, Locale.getDefault()));
        }

        try {
            newUser = userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new DataInputException(messageSource.getMessage("validation.invalidAccountInformation", null, Locale.getDefault()));
        }

        return userMapper.toUserListItem(newUser);
    }


    @Override
    public void changePassword(String id, String password) {

        User user = findById(id).get();

        boolean isPasswordValid = Pattern.matches(ValidationUtils.PASSWORD_REGEX, password);
        if (!isPasswordValid) {
            throw new InvalidPasswordException(messageSource.getMessage("validation.validPassword", null, Locale.getDefault()));
        }

        try {

            String newPasswordHash = passwordEncoder.encode(password);
            userRepository.changePassword(user.getId(), newPasswordHash);

        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.serverError", null, Locale.getDefault()));
        }
    }


    @Override
    public UserListItem changeStatus(String id) {

        User user = findById(id).get();
        if (user.getRole().getId() == 1) {
            throw new NotAllowDisableException(messageSource.getMessage("error.notAllow", null, Locale.getDefault()));
        }

        user.setDisabled(!user.isDisabled());

        try {
            user = userRepository.save(user);
            return userMapper.toUserListItem(user);
        } catch (Exception e) {
            throw new ServerErrorException(messageSource.getMessage("error.serverError", null, Locale.getDefault()));
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return UserPrinciple.build(userOptional.get());
    }

}
