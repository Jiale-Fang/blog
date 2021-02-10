package pers.fjl.extension.service;

import pers.fjl.extension.po.Link;
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
public interface LinkService extends IService<Link> {

    /**
     * 获取友链信息列表
     * @return list
     */
    List<Link> getLinkList();

    /**
     * 添加友链
     * @param link
     * @return boolean
     */
    boolean addLink(Link link);
}
