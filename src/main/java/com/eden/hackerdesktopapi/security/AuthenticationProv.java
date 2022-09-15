package com.eden.hackerdesktopapi.security;

import com.eden.hackerdesktopapi.constant.enums.ResultEnum;
import com.eden.hackerdesktopapi.constant.exceptions.CustomizedException;
import com.eden.hackerdesktopapi.service.intf.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;


/**
 * Customized user login authenticate
 *
 * @author Mo Xu
 * @date 2022/08/31
 */
@Component
public class AuthenticationProv implements AuthenticationProvider {
    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Check user login information
    @Override
    public Authentication authenticate(Authentication authentication) {
        // Get email and password from input
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Get userDetail from userService
        UserDetails userDetail = userService.loadUserByUsername(email);

        // Check if password is correct
        if (!passwordEncoder.matches(password, userDetail.getPassword())) {
            throw new CustomizedException(ResultEnum.FAILED, "Password incorrect");
        }

        Collection<? extends GrantedAuthority> authorities = userDetail.getAuthorities();

        return new UsernamePasswordAuthenticationToken(email, password, authorities);
    }

    // Used to support authentication class
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
