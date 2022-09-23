package com.eden.hackerdesktopapi.controller;


import com.eden.hackerdesktopapi.constant.consists.Result;
import com.eden.hackerdesktopapi.model.User;
import com.eden.hackerdesktopapi.service.intf.UserService;
import com.eden.hackerdesktopapi.utils.ReturnResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;


@RestController
@Validated
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/register")
    public Result<?> register(@RequestBody @Validated @NotNull User user) {
        userService.register(user);
        return ReturnResultUtil.success("Register success");
    }
}
