package com.eden.hackerdesktopapi.service.intf;

import com.eden.hackerdesktopapi.model.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * User service interfaces
 *
 * @author Mo Xu
 * @date 2022/08/31
 */
public interface UserService {
    /**
     * Method for spring security support
     *
     * @param email user's email
     * @return {@link UserDetails}
     */
    UserDetails loadUserByUsername(String email);

    /**
     * Register a new user
     *
     * @param user
     */
    void register(User user);
}
