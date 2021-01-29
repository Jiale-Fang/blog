package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Blog;
import pers.fjl.common.vo.AddBlogVo;
import pers.fjl.common.vo.BlogVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
public interface BlogService extends IService<Blog> {

    /**
     * 博客管理的分页数据
     * @param queryPageBean
     * @return Page<BlogVo>
     */
    Page<BlogVo> findPage(QueryPageBean queryPageBean,Long uid);

    /**
     * 添加博客
     * @param addBlogVo
     * @return boolean
     */
    boolean addBlog(AddBlogVo addBlogVo, Long uid);

    /**
     * 渲染首页的分页数据
     * @param queryPageBean
     * @param uid
     * @return Page<BlogVo>
     */
    Page<BlogVo> findHomePage(QueryPageBean queryPageBean);
}

