package com.yibo.usercenter.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: huangyibo
 * @Date: 2019/11/8 17:50
 * @Description:
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CheckAuthorization {

    String value();
}
