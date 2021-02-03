package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Tag;
import pers.fjl.common.vo.BlogVo;
import pers.fjl.common.vo.TagVo;
import pers.fjl.common.vo.TypeVo;

import java.util.List;

/**
 * <p>
 * 标签服务类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
public interface TagService extends IService<Tag> {

    /**
     * 获取后台管理的tag标签列表
     *
     * @return list
     */
    List<Tag> getTagList();

    /**
     * 获取每个分类的博客数量
     *
     * @return list
     */
    List<TagVo> getTagCount();

    /**
     * 获取后台管理标签列表
     * @param queryPageBean
     * @return page
     */
    Page<Tag> findPage(QueryPageBean queryPageBean);
}
