package pers.fjl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import pers.fjl.common.po.ChatLog;
import pers.fjl.common.vo.ChatLogVO;

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
     * @return list
     */
    List<ChatLog> getMessage(@Param("chatLogVO") ChatLogVO chatLogVO);
}
