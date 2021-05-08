package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.po.ChatLog;
import pers.fjl.common.vo.ChatLogVo;

import java.util.List;

public interface ChatLogService extends IService<ChatLog> {

    /**
     * 添加聊天记录
     * @param chatLog
     */
    void addMessage(ChatLog chatLog);

    /**
     * 获取和某人的聊天记录
     * @param chatLog
     * @return
     */
    List<ChatLogVo> getMessage(ChatLog chatLog);
}
