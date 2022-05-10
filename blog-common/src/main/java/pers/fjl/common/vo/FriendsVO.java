package pers.fjl.common.vo;

import lombok.Data;
import pers.fjl.common.po.Friends;

import java.io.Serializable;

@Data
public class FriendsVO extends Friends implements Serializable {
    private String username;
    private String nickname;
    private String avatar;
    private String lastContent;
    private Integer messageNum = 0; // 未读消息数量
}
