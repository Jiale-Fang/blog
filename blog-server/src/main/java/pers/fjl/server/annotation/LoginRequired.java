package pers.fjl.server.annotation;

import java.lang.annotation.*;

/**
 * 在需要登录验证的Controller的方法上使用此注解
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
