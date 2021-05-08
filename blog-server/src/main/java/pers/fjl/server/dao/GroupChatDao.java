package pers.fjl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import pers.fjl.common.po.ChatLog;
import pers.fjl.common.po.GroupChat;
import pers.fjl.common.vo.GroupChatVo;

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
public interface GroupChatDao extends BaseMapper<GroupChat> {

    @Select("SELECT u.username, u.nickname, g.content, g.create_time, u.avatar, g.uid " +
            "FROM group_chat g, user u " +
            "WHERE g.uid = u.uid " +
            "ORDER BY g.create_time ASC")
    List<GroupChatVo> getMessage();
}
