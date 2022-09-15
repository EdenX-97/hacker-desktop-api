package com.eden.hackerdesktopapi.constant.exceptions;

import com.eden.hackerdesktopapi.constant.enums.ResultEnum;
import lombok.Getter;


/**
 * Customized exception
 *
 * @author Mo Xu
 * @date 2022/09/01
 */
@Getter
public class CustomizedException extends RuntimeException {
    private ResultEnum code;
    private String message;

    public CustomizedException(ResultEnum code, String message) {
        this.code = code;
        this.message = message;
    }
}

