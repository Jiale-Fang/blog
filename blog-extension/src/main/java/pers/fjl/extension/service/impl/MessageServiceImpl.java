package pers.fjl.extension.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import pers.fjl.extension.po.Message;
import pers.fjl.extension.dao.MessageDao;
import pers.fjl.extension.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-02-08
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageDao, Message> implements MessageService {
    @Resource
    private MessageDao messageDao;

    @Cacheable(value = {"MessageMap"})
    public List<Message> getMessageList() {
        return messageDao.selectList(null);
    }

    @CacheEvict(value = {"MessageMap"})
    public boolean addMessage(Message message) {
        int i = messageDao.insert(message);
        if (i != 1) {
            throw new RuntimeException("添加留言失败");
        }
        return true;
    }
}
