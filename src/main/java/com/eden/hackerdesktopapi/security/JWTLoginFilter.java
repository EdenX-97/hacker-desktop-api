package com.eden.hackerdesktopapi.security;

import com.eden.hackerdesktopapi.utils.JWTUtil;
import com.eden.hackerdesktopapi.utils.ReturnResultUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Customized filter for login
 *
 * @author Mo Xu
 * @date 2022/09/01
 */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Create the UsernamePasswordAuthenticationToken
     *
     * @param req
     * @param res
     * @return {@link Authentication}
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        // Get email and password
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // 返回authentication
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password, new ArrayList<>()));
    }

    /**
     * When authentication success, return the JWT
     *
     * @param request
     * @param response
     * @param chain
     * @param auth
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication auth)
            throws IOException, ServletException {
        // Use util to generate the JWT
        String token = JWTUtil.generateToken(auth.getName());

        // Return the response with JWT
        response.addHeader(JWTUtil.authorization, token);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(ReturnResultUtil.success("Login success").toString());
    }

    /**
     * When authentication failed, return the information
     *
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(ReturnResultUtil.failed(exception.getMessage()).toString());
    }
}
