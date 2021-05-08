package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.po.Friends;
import pers.fjl.common.po.Views;
import pers.fjl.common.vo.FriendsVo;

import java.util.List;

public interface FriendsService extends IService<Friends> {

    /**
     * 获取某个用户的好友列表
     * @param uid
     * @return
     */
    List<FriendsVo> getFriendsList(Long uid);

    /**
     * 添加好友
     * @param friends
     */
    void addFriend(Friends friends);
}
