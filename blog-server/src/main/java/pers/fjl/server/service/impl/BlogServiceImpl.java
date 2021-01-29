package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Blog;
import pers.fjl.common.po.Type;
import pers.fjl.common.vo.AddBlogVo;
import pers.fjl.common.vo.BlogVo;
import pers.fjl.server.dao.BlogDao;
import pers.fjl.server.service.BlogService;
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
public class BlogServiceImpl extends ServiceImpl<BlogDao, Blog> implements BlogService {
    @Resource
    private BlogDao blogDao;
    @Resource
    private BlogTagService blogTagService;

    //    @Cacheable(value = {"Types2Map"})
    public Page<BlogVo> findPage(QueryPageBean queryPageBean, Long uid) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        Integer start = (currentPage - 1) * pageSize;

        //设置分页条件
        Page<BlogVo> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid);
//        if (queryPageBean.getQueryString() != null) {  //不为空，则代表是根据用户名条件查询
//            //查询总记录数
//            page.setTotal(blogDao.selectSearchCount(queryPageBean.getQueryString(), uid));
//            page.setRecords(blogDao.searchByUsername(page, queryPageBean.getQueryString()));
//            return page;
//        }
        //执行全部查询
        page.setRecords(blogDao.getAllBlogs(uid, start, pageSize));
        //查询总记录数
        page.setTotal(blogDao.selectCount(wrapper));
        return page;
    }

    @Transactional
    public boolean addBlog(AddBlogVo addBlogVo, Long uid) {
        Blog blog = new Blog();
        BeanUtils.copyProperties(addBlogVo, blog);
        blog.setUid(uid);
        Long blogId = IdWorker.getId(Blog.class);
        blog.setBlogId(blogId);
        blogDao.insert(blog);
        // 还要插入标签与博客的中间表
        blogTagService.addOneBlogTag(blogId, addBlogVo.getValue());
        return true;
    }

    @Transactional
    public Page<BlogVo> findHomePage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        Integer start = (currentPage - 1) * pageSize;
        //设置分页条件
        Page<BlogVo> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        page.setTotal(blogDao.selectCount(null));
        page.setRecords(blogDao.findHomePage(start,pageSize));
        return page;
    }
}
