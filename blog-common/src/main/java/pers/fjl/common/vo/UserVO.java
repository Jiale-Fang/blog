package pers.fjl.common.vo;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.fjl.common.po.User;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends User implements Serializable {
    private String verKey;  // 缓存在redis中的验证码的key
    private String code; // 登录时的验证码
}
