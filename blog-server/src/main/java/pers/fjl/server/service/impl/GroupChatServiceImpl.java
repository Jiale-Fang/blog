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
import java.util.ArrayList;
import java.util.List;

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
        for (GroupChatVo groupChatVo : groupMessageList) {
            if (groupChatVo.getUid().equals(uid)){
                groupChatVo.setType(1);
            }else {
                groupChatVo.setType(3);
            }
        }

        return groupMessageList;
    }

    @Override
    public void addMessage(GroupChat groupChat) {
        groupChatDao.insert(groupChat);
    }
}
