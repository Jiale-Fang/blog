package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.dto.BlogBackDTO;
import pers.fjl.common.dto.BlogBackInfoDTO;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Blog;
import pers.fjl.common.vo.AddBlogVo;
import pers.fjl.common.vo.BlogVo;

import java.util.List;

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
     * 渲染首页的分页数据(按阅读量降序)
     * @param queryPageBean
     * @return Page<BlogVo>
     */
    Page<BlogVo> findHomePage(QueryPageBean queryPageBean);

    /**
     * 根据博客id获取博客
     * @param blog_id
     * @return blog
     */
    BlogVo getOneBlog(Long blog_id);

    /**
     * 按照时间降序获取最新推荐的博客列表
     * @return list
     */
    List<Blog> getLatestList();

    /**
     * 根据分类id获取博客分页数据
     * @param queryPageBean
     * @return page
     */
    Page<BlogVo> getByTypeId(QueryPageBean queryPageBean);

    /**
     * 增加博客浏览量，直接调用getOneBlog方法会导致缓存，致使博客浏览量没有提升
     * @param blogId
     */
    void setViews(Long blogId);

    /**
     * 点赞
     * @param blogId 博客id
     * @param uid 用户id
     */
    boolean thumbsUp(Long blogId, Long uid);

    /**
     * 获取博客后台可视化管理数据
     * @return 博客数据dto
     */
    BlogBackInfoDTO getBlogBackInfo();

    /**
     * 获取博客后台分页数据
     * @param queryPageBean 分页实体
     * @return page
     */
    Page<BlogBackDTO> adminBlogPage(QueryPageBean queryPageBean);
}

