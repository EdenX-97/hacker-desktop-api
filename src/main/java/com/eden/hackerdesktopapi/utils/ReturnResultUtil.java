package com.eden.hackerdesktopapi.utils;

import com.eden.hackerdesktopapi.constant.consists.Result;
import com.eden.hackerdesktopapi.constant.enums.ResultEnum;
import org.springframework.stereotype.Component;


/**
 * Util for return the result
 *
 * @author Mo Xu
 * @date 2022/08/31
 */
@Component
public class ReturnResultUtil {
    public static Result success(Object data) {
        return new Result().setResult(ResultEnum.SUCCESS, data);
    }

    public static Result failed(Object data) {
        return new Result().setResult(ResultEnum.FAILED, data);
    }

    public static Result invalidParam(Object data) {
        return new Result().setResult(ResultEnum.INVALID_PARAM, data);
    }

    public static Result notAuthorized(Object data) {
        return new Result().setResult(ResultEnum.NOT_AUTHORIZED, data);
    }

    public static Result notFound(Object data) {
        return new Result().setResult(ResultEnum.NOT_FOUND, data);
    }

    public static Result databaseFailed(Object data) {
        return new Result().setResult(ResultEnum.DATABASE_FAILED, data);
    }

    public static Result unknownError(Object data) {
        return new Result().setResult(ResultEnum.UNKNOWN_ERROR, data);
    }
}
