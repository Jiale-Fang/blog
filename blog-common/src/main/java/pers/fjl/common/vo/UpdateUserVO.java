package pers.fjl.common.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import pers.fjl.common.po.User;
import pers.fjl.common.utils.JsonLongSerializer;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserVO implements Serializable {

    @JsonSerialize(using = JsonLongSerializer.class )
    @TableId(value = "uid")
    private Long uid;

    private String nickname;

    private String username;

    private String password;

    private String email;

    private String avatar;

    private String code; // 登录时的验证码

    /**
     * 登录类型
     */
    private Integer loginType;

}
