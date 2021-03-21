package pers.fjl.extension.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.vo.BlogVo;
import pers.fjl.extension.dao.CrawledBlogDao;
import pers.fjl.extension.dao.LinkDao;
import pers.fjl.extension.po.CrawledBlog;
import pers.fjl.extension.po.Link;
import pers.fjl.extension.service.CrawledBlogService;
import pers.fjl.extension.service.LinkService;

import javax.annotation.Resource;
import java.util.List;

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
        System.out.println("当前页" + queryPageBean.getCurrentPage() + "======>" + queryPageBean.getPageSize());
        return crawledBlogDao.selectPage(page, null);
    }

    @Cacheable(value = {"CrawledBlogMap"}, key = "#blogId")
    public CrawledBlog getOneBlog(Long blogId) {
        QueryWrapper<CrawledBlog> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_id", blogId);
        return crawledBlogDao.selectOne(wrapper);
    }
}
