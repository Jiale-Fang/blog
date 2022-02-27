package pers.fjl.encrypt.utils;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import pers.fjl.common.utils.JsonLongSerializer;

@Data
public class JwtUserInfo {
    @JsonSerialize(using = JsonLongSerializer.class )
    private Long uid;

    private String username;

    private String nickname;

    private Integer roleId;

}
