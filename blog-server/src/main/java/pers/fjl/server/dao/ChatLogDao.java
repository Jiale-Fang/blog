package pers.fjl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import pers.fjl.common.po.ChatLog;

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
public interface ChatLogDao extends BaseMapper<ChatLog> {

    /**
     * 获取某用户的聊天记录
     * @param sender
     * @param receiver
     * @return
     */
    @Select("SELECT msg_id,sender,receiver,create_time,content " +
            "FROM chat_log " +
            "WHERE (sender = #{sender} AND receiver = #{receiver}) " +
            "OR (sender = #{receiver} AND receiver = #{sender}) " +
            "ORDER BY create_time ASC ")
    List<ChatLog> getMessage(Long sender, Long receiver);
}
