package pers.fjl.extension.service;

import pers.fjl.extension.po.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fangjiale
 * @since 2021-02-08
 */
public interface MessageService extends IService<Message> {

    /**
     * 获取留言信息列表
     * @return list
     */
    List<Message> getMessageList();

    /**
     * 添加留言
     * @param message
     * @return boolean
     */
    boolean addMessage(Message message);
}
