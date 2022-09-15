package com.eden.hackerdesktopapi.constant.enums;

import lombok.Getter;


/**
 * Enum for user roles
 *
 * @author Mo Xu
 * @date 2022/08/31
 */
@Getter
public enum UserRoleEnum {
    DISABLE("ROLE_DISABLE"),
    USER("ROLE_USER"),
    BLOCKED("ROLE_BLOCKED"),
    ADMIN("ROLE_ADMIN");

    private final String role;

    UserRoleEnum(String role) {
        this.role = role;
    }
}
