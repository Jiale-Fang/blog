package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.po.Tag;

import java.util.List;

/**
 * <p>
 *  标签服务类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
public interface TagService extends IService<Tag> {

    /**
     * 获取tag标签列表
     * @return list
     */
    List<Tag> getTagList();
}
