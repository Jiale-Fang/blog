package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Message;
import pers.fjl.server.dao.MessageDao;
import pers.fjl.server.filter.SensitiveFilter;
import pers.fjl.server.service.MessageService;
import pers.fjl.server.utils.IpUtils;

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

    @Caching(evict = {
            @CacheEvict(value = {"MessagePage"}, allEntries = true),
            @CacheEvict(value = {"MessageMap"}, allEntries = true)
    })
    public boolean addMessage(Message message, String host) {
        message.setIp(host);
        String ipSource = IpUtils.getIpSource(host);
        message.setIpSource(ipSource);
        message.setMessageContent(SensitiveFilter.filter(message.getMessageContent()));
        int i = messageDao.insert(message);
        return i == 1;
    }

    @Cacheable(value = {"MessagePage"}, key = "#root.methodName+'['+#queryPageBean.currentPage+']'")
    public Page<Message> getMessagePage(QueryPageBean queryPageBean) {
        Page<Message> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.like(queryPageBean.getQueryString() != null, "nickname", queryPageBean.getQueryString());
        return messageDao.selectPage(page, wrapper);
    }

    @Caching(evict = {
            @CacheEvict(value = {"MessagePage"}, allEntries = true),
            @CacheEvict(value = {"MessageMap"}, allEntries = true)
    })
    @Override
    public void deleteMessage(List<Long> messageIdList) {
        messageDao.deleteBatchIds(messageIdList);
    }
}
