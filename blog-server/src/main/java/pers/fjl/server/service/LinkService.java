package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Link;

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

    /**
     * 获取友链分页数据
     * @param queryPageBean 分页实体
     * @return page
     */
    Page<Link> listLink(QueryPageBean queryPageBean);

    /**
     * 修改友链禁用状态
     *
     * @param link 友链禁用信息
     */
    void updateLinkDisable(Link link);

    /**
     * 保存或更新友链
     *
     * @param link 友链
     */
    void saveOrUpdateFriendLink(Link link);
}
