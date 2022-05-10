package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.BlogTag;
import pers.fjl.common.vo.BlogVO;

/**
 * <p>
 *  标签博客中间表服务类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
public interface BlogTagService extends IService<BlogTag> {

    boolean addOneBlogTag(Long blogId, Integer[] value);

    /**
     * 根据标签id获取博客分页数据
     * @param queryPageBean
     * @return page
     */
    Page<BlogVO> getByTagId(QueryPageBean queryPageBean);
}
