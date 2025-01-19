package com.example.api;

import lombok.Getter;

/**
 * Author:Niu
 * Date:2025/1/18 10:19
 */
@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    UNAUTHORIZED(401, "未授权"),
    NOT_FOUND(404, "参数检验失败"),

    ;

    private final long code;
    private final String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }
}
