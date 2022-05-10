package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.po.ChatLog;
import pers.fjl.common.vo.ChatLogVO;

import java.util.List;
import java.util.Map;

public interface ChatLogService extends IService<ChatLog> {

    /**
     * 添加聊天记录
     * @param chatLog
     */
    void addMessage(ChatLog chatLog);

    /**
     * 获取和某人的聊天记录
     * @param chatLogVO 聊天信息
     * @return map
     */
    Map<String, List<?>> getMessage(ChatLogVO chatLogVO);
}
