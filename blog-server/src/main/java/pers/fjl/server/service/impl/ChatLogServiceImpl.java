package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import pers.fjl.common.po.ChatLog;
import pers.fjl.common.vo.ChatLogVo;
import pers.fjl.server.dao.ChatLogDao;
import pers.fjl.server.service.ChatLogService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 消息服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-0-12
 */
@Service
public class ChatLogServiceImpl extends ServiceImpl<ChatLogDao, ChatLog> implements ChatLogService {
    @Resource
    private ChatLogDao chatLogDao;

    @Override
    public void addMessage(ChatLog chatLog) {
        chatLogDao.insert(chatLog);
    }

    @Override
    public List<ChatLogVo> getMessage(ChatLog chatLog) {
//        QueryWrapper<ChatLog> wrapper = new QueryWrapper<>();
        List<ChatLogVo> chatLogVos = new ArrayList<>();
        List<ChatLog> chatLogs = chatLogDao.getMessage(chatLog.getSender(),chatLog.getReceiver());
//        wrapper.eq("sender", chatLog.getSender())
//                .or()
//                .eq("sender", chatLog.getReceiver())
//                .eq("receiver", chatLog.getReceiver())
//                .or()
//                .eq("receiver", chatLog.getSender())
//                .orderByAsc("create_time");
//        // 发送者和接收者是相对的，要解决这个问题
//        List<ChatLog> chatLogs = chatLogDao.selectList(wrapper);
        for (ChatLog chatLog1 : chatLogs) {
            ChatLogVo chatLogVo = new ChatLogVo();
            BeanUtils.copyProperties(chatLog1, chatLogVo);
            if (chatLog.getSender().equals(chatLogVo.getSender())){ // 代表发送者就是自己
                chatLogVo.setType(1);
            }else {
                chatLogVo.setType(2);
            }
            chatLogVos.add(chatLogVo);
        }

        return chatLogVos;
    }
}
