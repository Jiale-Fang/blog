package pers.fjl.common.vo;

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
 * @since 2022-02-28
 */
@Data
public class ResetPasswordVO implements Serializable {
    private String username;

    private String password;

    private String email;

    private String code; // 邮箱验证码
}
