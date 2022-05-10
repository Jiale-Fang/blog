package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import pers.fjl.common.po.ChatLog;
import pers.fjl.common.dto.ChatLogDTO;
import pers.fjl.common.vo.ChatLogVO;
import pers.fjl.server.dao.ChatLogDao;
import pers.fjl.server.service.ChatLogService;
import pers.fjl.server.utils.ChatLogTimeUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * <p>
 * 消息服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-04-12
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
    public Map<String, List<?>> getMessage(ChatLogVO chatLogVO) {
        List<ChatLogDTO> chatLogDTOS = new ArrayList<>();
        List<ChatLog> chatLogs = chatLogDao.getMessage(chatLogVO);
        Long currentTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        ArrayList<String> voiceIdList = new ArrayList<>();
        // 发送者和接收者是相对的，要解决这个问题
        for (ChatLog chatLog1 : chatLogs) {
            ChatLogDTO chatLogDTO = new ChatLogDTO();
            BeanUtils.copyProperties(chatLog1, chatLogDTO);
            switch (chatLog1.getTextType()) {
                case 1: // 文字消息
                    if (chatLogVO.getSender().equals(chatLogDTO.getSender())) { // 代表发送者就是自己
                        chatLogDTO.setType(1);
                    } else {
                        chatLogDTO.setType(2);
                    }
                    break;
                case 2: // 图片消息
                    if (chatLogVO.getSender().equals(chatLogDTO.getSender())) { // 代表发送者就是自己
                        chatLogDTO.setType(7);
                    } else {
                        chatLogDTO.setType(8);
                    }
                    break;
                case 3: // 语音消息
                    if (chatLogVO.getSender().equals(chatLogDTO.getSender())) { // 代表发送者就是自己
                        chatLogDTO.setType(9);
                    } else {
                        chatLogDTO.setType(10);
                    }
                    voiceIdList.add(chatLog1.getMsgId().toString());
                    break;
            }
            chatLogDTOS.add(chatLogDTO);
            LocalDateTime ldt = chatLogDTO.getCreateTime();
            Long time = ldt.toInstant(ZoneOffset.of("+8")).toEpochMilli();  //获取聊天记录的毫秒值
            if (Math.abs(currentTime - time) >= 5 * 60 * 1000) {   //两条聊天记录间隔超过五分钟就要显示他的时间
                currentTime = time;
                ChatLogDTO chatLogTimeVo = new ChatLogDTO();
                chatLogTimeVo.setType(6);   // 单纯显示时间的消息
                chatLogTimeVo.setFormatTime(ChatLogTimeUtils.formatTime(ldt));
                chatLogDTOS.add(chatLogTimeVo);
            }
        }
        Map<String, List<?>> listMap = new HashMap<>();
        // 查询的时候是倒序查的最后15条数据，所以需要倒排
        Collections.reverse(chatLogDTOS);
        Collections.reverse(voiceIdList);
        listMap.put("chatLogList", chatLogDTOS);
        listMap.put("voiceIdList", voiceIdList);
        return listMap;
    }
}
