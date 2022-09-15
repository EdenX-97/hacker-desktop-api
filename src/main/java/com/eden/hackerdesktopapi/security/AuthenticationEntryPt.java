package com.eden.hackerdesktopapi.security;

import com.eden.hackerdesktopapi.utils.ReturnResultUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Customized unlogin user not authorization
 *
 * @author Mo Xu
 * @date 2022/08/31
 */
@Component
public class AuthenticationEntryPt implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter()
                .write(ReturnResultUtil.notAuthorized("No authorization").toString());
    }
}
