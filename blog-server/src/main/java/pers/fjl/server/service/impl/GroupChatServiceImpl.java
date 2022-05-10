package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pers.fjl.common.po.GroupChat;
import pers.fjl.common.vo.GroupChatVO;
import pers.fjl.server.dao.GroupChatDao;
import pers.fjl.server.filter.SensitiveFilter;
import pers.fjl.server.service.GroupChatService;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

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
    public Map<String, List<?>> getMessage(Long uid, Integer currentPage) {
        List<GroupChatVO> groupMessageList = groupChatDao.getMessage(currentPage * 15);
        ArrayList<String> voiceIdList = new ArrayList<>();
        Long currentTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();   //当前时间
        for (int i = 0; i < groupMessageList.size(); i++) {
            switch (groupMessageList.get(i).getTextType()) {
                case 1: // 文字消息
                    if (groupMessageList.get(i).getUid().equals(uid)) {  //代表是自己发出的消息
                        groupMessageList.get(i).setType(1);
                    } else if (groupMessageList.get(i).getUid() != null) {
                        groupMessageList.get(i).setType(3);
                    }
                    break;
                case 2: // 图片消息
                    if (groupMessageList.get(i).getUid().equals(uid)) {  //代表是自己发出的图片消息
                        groupMessageList.get(i).setType(7);
                    } else if (groupMessageList.get(i).getUid() != null) {
                        groupMessageList.get(i).setType(8);
                    }
                    break;
                case 3: // 语音消息
                    if (groupMessageList.get(i).getUid().equals(uid)) {  //代表是自己发出的图片消息
                        groupMessageList.get(i).setType(9);
                    } else if (groupMessageList.get(i).getUid() != null) {
                        groupMessageList.get(i).setType(10);
                    }
                    voiceIdList.add(groupMessageList.get(i).getMsgId().toString());
                    break;
            }
            LocalDateTime ldt = groupMessageList.get(i).getCreateTime();
            Long time = ldt.toInstant(ZoneOffset.of("+8")).toEpochMilli();  //获取聊天记录的毫秒值
            if (Math.abs(currentTime - time) >= 5 * 60 * 1000) {   //两条聊天记录间隔超过五分钟就要显示他的时间
                currentTime = time;
                GroupChatVO groupChatVO = new GroupChatVO();
                groupChatVO.setType(6);
                groupChatVO.setFormatTime(formatTime(ldt));
                groupMessageList.add(i+1, groupChatVO);    //到时候要翻转list所以时间先放在消息后面
                i++;
            }
        }
        Map<String, List<?>> listMap = new HashMap<>();
        // 查询的时候是倒序查的最后15条数据，所以需要倒排
        Collections.reverse(groupMessageList);
        Collections.reverse(voiceIdList);
        listMap.put("groupMessageList", groupMessageList);
        listMap.put("voiceIdList", voiceIdList);
        return listMap;
    }

    @Override
    public void addMessage(GroupChat groupChat) {
        if (groupChat.getTextType().equals(1))  // 文字消息
            groupChat.setContent(SensitiveFilter.filter(groupChat.getContent()));
        groupChatDao.insert(groupChat);
    }
}
