package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.fjl.common.po.Friends;
import pers.fjl.common.vo.FriendsVO;
import pers.fjl.server.dao.FriendsDao;
import pers.fjl.server.service.FriendsService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 好友关系服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-04-12
 */
@Service
public class FriendsServiceImpl extends ServiceImpl<FriendsDao, Friends> implements FriendsService {
    @Resource
    private FriendsDao friendsDao;

    @Override
    public List<FriendsVO> getFriendsList(Long uid) {
        List<FriendsVO> friendsList = friendsDao.getFriendsList(uid);
        String lastContent = "";
        for (int i = 0; i < friendsList.size(); i++) {
            if (friendsDao.findLastContent(uid, friendsList.get(i).getFriendId()) != null) {
                FriendsVO voDb = friendsDao.findLastContent(uid, friendsList.get(i).getFriendId());
                lastContent = voDb.getLastContent();
                friendsList.get(i).setCreateTime(voDb.getCreateTime());
                if (lastContent.length() >= 12) // 太长了无法显示在聊天区域
                    lastContent = lastContent.substring(0, 11) + "...";
            } else {
                lastContent = "暂无与该用户的聊天记录";
            }
            friendsList.get(i).setLastContent(lastContent);
        }
        return friendsList;
    }

    @Transactional
    public void addFriend(Friends friends) {
        Friends friends1 = new Friends();
        friends1.setFriendId(friends.getUid());
        friends1.setUid(friends.getFriendId());
        Friends friendsDB = friendsDao.selectOne(new LambdaQueryWrapper<Friends>().eq(Friends::getUid, friends.getUid())
                .eq(Friends::getFriendId, friends.getFriendId()));
        if (friendsDB == null) {   // 证明没添加过好友
            friendsDao.insert(friends);
            friendsDao.insert(friends1);
        }
    }
}
