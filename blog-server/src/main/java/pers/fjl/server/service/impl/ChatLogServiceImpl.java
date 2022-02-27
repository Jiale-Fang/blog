package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import pers.fjl.common.po.ChatLog;
import pers.fjl.common.vo.ChatLogVo;
import pers.fjl.common.vo.GroupChatVo;
import pers.fjl.server.dao.ChatLogDao;
import pers.fjl.server.service.ChatLogService;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static pers.fjl.server.utils.ChatLogTimeUtils.formatTime;

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

        List<ChatLogVo> chatLogVos = new ArrayList<>();
        List<ChatLog> chatLogs = chatLogDao.getMessage(chatLog.getSender(), chatLog.getReceiver());
        Long currentTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        // 发送者和接收者是相对的，要解决这个问题
        for (ChatLog chatLog1 : chatLogs) {
            ChatLogVo chatLogVo = new ChatLogVo();
            BeanUtils.copyProperties(chatLog1, chatLogVo);
            if (chatLog1.getTextType() == 1) {  //代表是文本消息
                if (chatLog.getSender().equals(chatLogVo.getSender())) { // 代表发送者就是自己
                    chatLogVo.setType(1);
                } else {
                    chatLogVo.setType(2);
                }
            } else {    //代表只是普通的文本消息
                if (chatLog.getSender().equals(chatLogVo.getSender())) { // 代表发送者就是自己
                    chatLogVo.setType(7);
                } else {
                    chatLogVo.setType(8);
                }
            }
            LocalDateTime ldt = chatLogVo.getCreateTime();
            Long time = ldt.toInstant(ZoneOffset.of("+8")).toEpochMilli();  //获取聊天记录的毫秒值
            if (Math.abs(currentTime - time) >= 5 * 60 * 1000) {   //两条聊天记录间隔超过五分钟就要显示他的时间
                currentTime = time;
                ChatLogVo chatLogTimeVo = new ChatLogVo();
                chatLogTimeVo.setType(6);
                chatLogTimeVo.setFormatTime(formatTime(ldt));
                chatLogVos.add(chatLogTimeVo);
            }
            chatLogVos.add(chatLogVo);
        }

        return chatLogVos;
    }
}
