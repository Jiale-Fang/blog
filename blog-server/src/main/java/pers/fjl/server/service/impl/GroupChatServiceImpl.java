package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import pers.fjl.common.po.ChatLog;
import pers.fjl.common.po.GroupChat;
import pers.fjl.common.vo.ChatLogVo;
import pers.fjl.common.vo.GroupChatVo;
import pers.fjl.server.dao.ChatLogDao;
import pers.fjl.server.dao.GroupChatDao;
import pers.fjl.server.service.ChatLogService;
import pers.fjl.server.service.GroupChatService;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static pers.fjl.server.utils.ChatLogTimeUtils.formatTime;

/**
 * <p>
 * 群聊消息服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-0-12
 */
@Service
public class GroupChatServiceImpl extends ServiceImpl<GroupChatDao, GroupChat> implements GroupChatService {
    @Resource
    private GroupChatDao groupChatDao;

    @Override
    public List<GroupChatVo> getMessage(Long uid) {
        List<GroupChatVo> groupMessageList = groupChatDao.getMessage();
        Long currentTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();   //当前时间
        for (int i = 0; i < groupMessageList.size(); i++) {
            LocalDateTime ldt = groupMessageList.get(i).getCreateTime();
            Long time = ldt.toInstant(ZoneOffset.of("+8")).toEpochMilli();  //获取聊天记录的毫秒值
            if (Math.abs(currentTime - time) >= 5 * 60 * 1000) {   //两条聊天记录间隔超过五分钟就要显示他的时间
                currentTime = time;
                GroupChatVo groupChatVo = new GroupChatVo();
                groupChatVo.setType(6);
                groupChatVo.setFormatTime(formatTime(ldt));
                groupMessageList.add(i, groupChatVo);    //聊天时间显示替代了之前聊天记录的位置，所以要把原来聊天记录的索引挪后一位
                i++;
            }
            if (groupMessageList.get(i).getTextType()!=2){  //代表不是图片类型的消息
                if (groupMessageList.get(i).getUid().equals(uid)) {  //代表是自己发出的消息
                    groupMessageList.get(i).setType(1);
                } else if (groupMessageList.get(i).getUid() != null) {
                    groupMessageList.get(i).setType(3);
                }
            } else {
                if (groupMessageList.get(i).getUid().equals(uid)) {  //代表是自己发出的图片消息
                    groupMessageList.get(i).setType(7);
                } else if (groupMessageList.get(i).getUid() != null) {
                    groupMessageList.get(i).setType(8);
                }
            }
        }

        return groupMessageList;
    }

    @Override
    public void addMessage(GroupChat groupChat) {
        groupChatDao.insert(groupChat);
    }
}
