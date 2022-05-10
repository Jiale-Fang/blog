package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.dto.ViewsDTO;
import pers.fjl.common.po.Views;

import java.util.List;

public interface ViewsService extends IService<Views> {

    /**
     * 获取七天全站浏览量数据
     * @return list
     */
    List<ViewsDTO> getViewsData();

}
