package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.BlogTag;
import pers.fjl.common.vo.BlogVO;
import pers.fjl.server.dao.BlogTagDao;
import pers.fjl.server.service.BlogTagService;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagDao, BlogTag> implements BlogTagService {
    @Resource
    private BlogTagDao blogTagDao;

    private Integer currentPage;
    private Integer pageSize;
    private Integer start;

    @Transactional
    @CacheEvict(value = {"BlogPage"}, allEntries = true)
    public boolean addOneBlogTag(Long blogId, Integer[] value) {
        BlogTag blogTag = new BlogTag();
        blogTag.setBlogId(blogId);
        for (Integer tag : value) {
            blogTag.setTagId(tag);
            blogTagDao.insert(blogTag);
        }
        return true;
    }

    @Cacheable(value = {"BlogPage"}, key = "#root.methodName+'['+#queryPageBean.tagId+']'")
    public Page<BlogVO> getByTagId(QueryPageBean queryPageBean) {
        currentPage = queryPageBean.getCurrentPage();
        pageSize = queryPageBean.getPageSize();
        start = (currentPage - 1) * pageSize;
        //设置分页条件
        Page<BlogVO> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<BlogTag> wrapper = new QueryWrapper<>();
        wrapper.eq(queryPageBean.getTagId() != null, "tag_id", queryPageBean.getTagId());
        page.setTotal(blogTagDao.selectCount(wrapper));
        page.setRecords(blogTagDao.getByTagId(start, pageSize, queryPageBean.getTagId()));
        return page;
    }
}
