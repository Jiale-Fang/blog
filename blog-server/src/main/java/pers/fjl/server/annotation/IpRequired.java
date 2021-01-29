package pers.fjl.server.annotation;

import java.lang.annotation.*;

/**
 * 限制一个设备的IP登录
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IpRequired {
}
