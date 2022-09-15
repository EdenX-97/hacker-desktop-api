package com.eden.hackerdesktopapi.constant.enums;

import lombok.Getter;


@Getter
public enum ResultEnum {
    SUCCESS(1000, "Success"), // 成功代码
    FAILED(2000, "Failed"), // 操作失败代码
    INVALID_PARAM(2001, "Invalid param"), // 非法参数代码
    NOT_AUTHORIZED(2002, "No authorized"), // 请求无权限代码
    NOT_FOUND(2003, "Not found"), // 未找到对应api代码
    DATABASE_FAILED(2004, "Call database failed"), // 调用数据库失败
    UNKNOWN_ERROR(5000, "Unknown error"); // 未知错误代码

    private final Integer code;
    private final String message;

    ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
