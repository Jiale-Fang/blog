package pers.fjl.common.vo;

import lombok.Data;
import pers.fjl.common.po.GroupChat;

import java.io.Serializable;

@Data
public class GroupChatVo extends GroupChat implements Serializable {
    private String avatar;  // 用户头像
    private String username;
    private String nickname;
    private Long uid;
    private Integer type;
}
