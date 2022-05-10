package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.CrawledBlog;
import pers.fjl.server.dao.CrawledBlogDao;
import pers.fjl.server.service.CrawledBlogService;


import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-02-08
 */
@Service
public class CrawledBlogServiceImpl extends ServiceImpl<CrawledBlogDao, CrawledBlog> implements CrawledBlogService {
    @Resource
    private CrawledBlogDao crawledBlogDao;

    @Cacheable(value = {"crawlerPage"}, key = "#root.methodName+'['+#queryPageBean.currentPage+']'")
    public Page<CrawledBlog> crawlerPage(QueryPageBean queryPageBean) {
        Page<CrawledBlog> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        return crawledBlogDao.selectPage(page, null);
    }

    @Cacheable(value = {"CrawledBlogMap"}, key = "#blogId")
    public CrawledBlog getOneBlog(Long blogId) {
        QueryWrapper<CrawledBlog> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_id", blogId);
        return crawledBlogDao.selectOne(wrapper);
    }
}
