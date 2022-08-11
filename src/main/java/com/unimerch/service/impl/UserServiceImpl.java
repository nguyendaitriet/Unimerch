package com.unimerch.service.impl;


import com.unimerch.dto.UserCreateParam;
import com.unimerch.dto.UserCreateResult;
import com.unimerch.exception.InvalidIdException;
import com.unimerch.exception.InvalidPasswordException;
import com.unimerch.exception.ServerErrorException;
import com.unimerch.mapper.UserMapper;
import com.unimerch.repository.UserRepository;
import com.unimerch.repository.model.Role;
import com.unimerch.repository.model.User;
import com.unimerch.repository.model.UserPrinciple;
import com.unimerch.service.UserService;
import com.unimerch.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserCreateResult create(UserCreateParam userCreateParam) {
        User newUser = userMapper.toUser(userCreateParam);

        newUser.setSalt("abc");
        newUser.setPasswordHash(passwordEncoder.encode(userCreateParam.getPassword()));
        newUser.setRole(new Role(2));


        newUser = userRepository.save(newUser);

        UserCreateResult userCreateResult = userMapper.toUserCreateResult(newUser);

        return userCreateResult;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void changePassword(String id, String password) {

        boolean isIdValid = Pattern.matches(ValidationUtils.ID_REGEX, id);
        boolean isPasswordValid = Pattern.matches(ValidationUtils.PASSWORD_REGEX, password);

        if (!isIdValid) {
            throw new InvalidIdException(ValidationUtils.ID_NOT_EXIST);
        }

        int validId = Integer.parseInt(id);
        if (!userRepository.existsById(validId)) {
            throw new InvalidIdException(ValidationUtils.ID_NOT_EXIST);
        }

        if (!isPasswordValid) {
            throw new InvalidPasswordException(ValidationUtils.VALID_PASSWORD);
        }

        try {

            String newPasswordHash = passwordEncoder.encode(password);
            userRepository.changePassword(validId, newPasswordHash);

        } catch (Exception e) {
            throw new ServerErrorException(ValidationUtils.SERVER_ERROR);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return UserPrinciple.build(userOptional.get());
//        return (UserDetails) userOptional.get();
    }

}
