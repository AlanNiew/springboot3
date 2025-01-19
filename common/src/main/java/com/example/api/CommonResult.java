package com.example.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Author:Niu
 * Date:2025/1/18 10:17
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResult<T> {

    private long code;
    private String message;
    private T data;
    private long timestamp = System.currentTimeMillis();

    public CommonResult() {}

    public CommonResult(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResult<T> success() {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    public static <T> CommonResult<T> fail(T data) {
        return new CommonResult<>(ResultCode.FAIL.getCode(), ResultCode.FAIL.getMessage(), data);
    }

    public static <T> CommonResult<T> fail(T data, String message) {
        return new CommonResult<>(ResultCode.FAIL.getCode(), message, data);
    }

    public static <T> CommonResult<T> fail(ResultCode resultCode) {
        return new CommonResult<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    public static <T> CommonResult<T> fail( String message) {
        return new CommonResult<>(ResultCode.FAIL.getCode(), message, null);
    }

}
