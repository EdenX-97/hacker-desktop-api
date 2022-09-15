package com.eden.hackerdesktopapi.handler;

import com.eden.hackerdesktopapi.constant.consists.Result;
import com.eden.hackerdesktopapi.constant.enums.ResultEnum;
import com.eden.hackerdesktopapi.constant.exceptions.CustomizedException;
import com.eden.hackerdesktopapi.utils.ReturnResultUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Handle the customized exception
 *
 * @author Mo Xu
 * @date 2022/09/01
 */
@RestControllerAdvice
public class CustomizedExceptionHandler {
    @ExceptionHandler(CustomizedException.class)
    public Result<?> UExceptionHandler(CustomizedException e) {
        String message = e.getMessage();
        ResultEnum code = e.getCode();

        switch (code) {
            case FAILED:
                return ReturnResultUtil.failed(message);
            case INVALID_PARAM:
                return ReturnResultUtil.invalidParam(message);
            case NOT_AUTHORIZED:
                return ReturnResultUtil.notAuthorized(message);
            case DATABASE_FAILED:
                return ReturnResultUtil.databaseFailed(message);
            case UNKNOWN_ERROR:
                return ReturnResultUtil.unknownError(message);
        }

        return ReturnResultUtil.unknownError(message);
    }
}

