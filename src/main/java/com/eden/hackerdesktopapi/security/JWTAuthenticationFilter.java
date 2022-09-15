package com.eden.hackerdesktopapi.security;

import com.eden.hackerdesktopapi.constant.enums.ResultEnum;
import com.eden.hackerdesktopapi.constant.exceptions.CustomizedException;
import com.eden.hackerdesktopapi.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Customized filter for JWT authentication
 *
 * @author Mo Xu
 * @date 2022/09/01
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * Get JWT from request and validate
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     * @throws RuntimeException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException, RuntimeException {
        // Get token from request header
        String token = request.getHeader(JWTUtil.authorization);
        if (token == null || token.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        // User getAuthentication function to validate token
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response, token);

        // Set the authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    /**
     * Validate token function
     *
     * @param request
     * @param response
     * @param token
     * @return {@link UsernamePasswordAuthenticationToken}
     * @throws RuntimeException
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request,
                                                                  HttpServletResponse response, String token) throws RuntimeException {
        // Token must start with correct prefix
        if (!token.startsWith("Bearer ")) {
            throw new CustomizedException(ResultEnum.INVALID_PARAM, "Token is incorrect");
        }

        // Parse token to claims which includes token information
        Claims claims = JWTUtil.parseToken(token);

        // Get create time and expire time from claims
        //long issuedAt = claims.getIssuedAt().getTime();
        long expirationTime = claims.getExpiration().getTime();

        long nowTime = System.currentTimeMillis();

        // Token is expire
        if (nowTime > expirationTime) {
            throw new CustomizedException(ResultEnum.FAILED, "Token is expired");
        }

        // // If now time is more than create time + half of expire time, and less than expire time, refresh the token
        // if ((issuedAt + ((expirationTime - issuedAt) / 2)) < nowTime && nowTime < expirationTime) {
        //     // Create and set refreshed token
        //     String refreshToken = JWTUtil.refreshToken(claims);
        //     response.setHeader(JWTUtil.authorization, refreshToken);
        // }

        // Get user email from claims, return the authorities
        String email = claims.get("email").toString();
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        return new UsernamePasswordAuthenticationToken(email, null, authorities);
    }
}