package pers.fjl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import pers.fjl.common.po.Friends;
import pers.fjl.common.po.Views;
import pers.fjl.common.vo.FriendsVo;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fangjiale
 * @since 2021-04-12
 */
@Repository
public interface FriendsDao extends BaseMapper<Friends> {

    @Select("SELECT f.friend_id, u.nickname, u.username, u.avatar " +
            "FROM friends f, user u " +
            "WHERE f.friend_id = u.uid and f.uid = #{uid} ")
    List<FriendsVo> getFriendsList(Long uid);

    @Select("SELECT c.content lastContent, c.create_time createTime " +
            "FROM chat_log c " +
            "WHERE (sender = #{uid} AND receiver = #{friendId}) " +
            "OR (sender = #{friendId} AND receiver = #{uid}) " +
            "ORDER BY c.create_time DESC " +
            "LIMIT 0,1")
    FriendsVo findLastContent(Long uid, Long friendId);
}
