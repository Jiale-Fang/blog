package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.po.ChatLog;
import pers.fjl.common.po.GroupChat;
import pers.fjl.common.vo.ChatLogVo;
import pers.fjl.common.vo.GroupChatVo;

import java.util.List;

public interface GroupChatService extends IService<GroupChat> {

    /**
     * 获取群聊消息
     * @return
     */
    List<GroupChatVo> getMessage(Long uid);

    /**
     * 添加群聊消息
     * @param groupChat
     */
    void addMessage(GroupChat groupChat);
}
