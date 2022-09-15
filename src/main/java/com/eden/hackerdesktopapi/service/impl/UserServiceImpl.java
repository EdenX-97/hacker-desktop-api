package com.eden.hackerdesktopapi.service.impl;

import com.eden.hackerdesktopapi.constant.enums.ResultEnum;
import com.eden.hackerdesktopapi.constant.exceptions.CustomizedException;
import com.eden.hackerdesktopapi.model.User;
import com.eden.hackerdesktopapi.repository.UserRepository;
import com.eden.hackerdesktopapi.service.intf.UserService;
import com.eden.hackerdesktopapi.utils.RegexUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * Implementation of user service
 *
 * @author Mo Xu
 * @date 2022/09/01
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        // Get user
        User user = getUserByEmail(email);

        // Return a class from spring security
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole())
        );
    }

    @Override
    public void register(User user) {
        // Check if email already be registered
        if (checkUserExist(user.getEmail())) {
            throw new CustomizedException(ResultEnum.INVALID_PARAM, "User email already exist");
        }

        System.out.println(user);

        // New a user variable to store information, avoid some malicious information
        User savedUser = new User();
        savedUser.setEmail(user.getEmail());
        // Encode the password
        savedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        // Save the user to database
        userRepository.save(savedUser);
    }

    /**
     * Private function for check if the user exist
     *
     * @param email User's email
     * @return {@link Boolean}
     */
    private Boolean checkUserExist(String email) {
        // Check if email is valid
        if (!RegexUtil.checkEmail(email)) {
            throw new CustomizedException(ResultEnum.INVALID_PARAM, "Email format wrong");
        }

        // Check if user exist
        return userRepository.existsByEmail(email);
    }

    /**
     * Private function for get the user data
     *
     * @param email User's email
     * @return {@link User}
     */
    private User getUserByEmail(String email) {
        if (!checkUserExist(email)) {
            throw new CustomizedException(ResultEnum.NOT_FOUND, "User not exist");
        }

        return userRepository.findUserByEmail(email);
    }
}
