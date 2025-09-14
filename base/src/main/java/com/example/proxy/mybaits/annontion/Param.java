package com.example.proxy.mybaits.annontion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Niu
 * @Date: 2025/5/22 14:06
 * @Description: 参数注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {
    //参数名称
    String value();
}
