package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.po.Views;

public interface ViewsService extends IService<Views> {

    /**
     * 添加博客浏览量的记录
     * @param blogId
     * @param host
     * @return boolean
     */
    boolean addViews(Long blogId, String host);
}
