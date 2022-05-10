package pers.fjl.common.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.fjl.common.utils.JsonLongSerializer;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


/**
 * 后台用户列表
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBackDTO {

    /**
     * id
     */
    @JsonSerialize(using = JsonLongSerializer.class )
    private Long uid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户角色
     */
    private List<UserRoleDTO> roleList;

    /**
     * 用户登录ip
     */
    private String lastIp;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 创建时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    /**
     * 最近登录时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastLoginTime;

    /**
     * 状态
     */
    private Integer status;

    private Integer loginType;

}
