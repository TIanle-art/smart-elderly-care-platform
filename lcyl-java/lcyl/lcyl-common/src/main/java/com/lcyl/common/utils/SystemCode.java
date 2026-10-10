package com.lcyl.common.utils;

public enum SystemCode {
    OK(200, "成功"),
    USERNAME_EXISTS(401, "用户名已存在"),
    USERNAME_ERROR(402, "用户名或密码错误"),
    NO_USER(403, "用户不存在"),
    NOT_LOGIN(404, "用户未登录"),
    NO_PERMISSION(405, "权限不足，禁止访问"),
    ERROR(500, "失败");
    int code;
    String message;

    SystemCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}