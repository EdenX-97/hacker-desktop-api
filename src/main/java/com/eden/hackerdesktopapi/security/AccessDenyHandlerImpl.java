package com.eden.hackerdesktopapi.security;

import com.eden.hackerdesktopapi.utils.ReturnResultUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Customized login user no authorization
 *
 * @author Mo Xu
 * @date 2022/08/31
 */
@Component
public class AccessDenyHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter()
                .write(ReturnResultUtil.notAuthorized("User have no authorization").toString());
    }
}
