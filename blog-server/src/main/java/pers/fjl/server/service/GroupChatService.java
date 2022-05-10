package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.po.GroupChat;

import java.util.List;
import java.util.Map;

public interface GroupChatService extends IService<GroupChat> {

    /**
     * 获取群聊消息
     * @return
     */
    Map<String, List<?>> getMessage(Long uid, Integer currentPage);

    /**
     * 添加群聊消息
     * @param groupChat
     */
    void addMessage(GroupChat groupChat);
}
