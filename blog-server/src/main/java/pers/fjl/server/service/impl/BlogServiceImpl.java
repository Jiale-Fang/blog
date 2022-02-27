package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.fjl.common.dto.*;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.*;
import pers.fjl.common.vo.AddBlogVo;
import pers.fjl.common.vo.BlogVo;
import pers.fjl.common.vo.TypeVo;
import pers.fjl.server.dao.*;
import pers.fjl.server.service.BlogService;
import pers.fjl.server.service.BlogTagService;
import pers.fjl.server.service.UserService;
import pers.fjl.server.service.ViewsService;
import pers.fjl.server.utils.MarkdownUtils;

import javax.annotation.Resource;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * <p>
 * 博客服务实现类
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
    @Resource
    private UserService userService;
    @Resource
    private ViewsService viewsService;
    @Resource
    private ThumbsUpDao thumbsUpDao;
    @Resource
    private MessageDao messageDao;
    @Resource
    private ViewsDao viewsDao;
    @Resource
    private UserDao userDao;
    @Resource
    private TypeDao typeDao;
    @Resource
    private TagDao tagDao;

    private Integer currentPage;
    private Integer pageSize;
    private Integer start;

    /**
     * 获取后台首页信息
     *
     * @return blogbackinfodto
     */
    public BlogBackInfoDTO getBlogBackInfo() {
        // 查询访问量
        Integer viewsCount = viewsDao.selectCount(null);
        // 查询留言量
        Integer messageCount = messageDao.selectCount(null);
        // 查询用户量
        Integer userCount = userDao.selectCount(null);
        // 查询文章量
        Integer articleCount = blogDao.selectCount(null);
        // 查询一周访问量
        List<ViewsDTO> viewsDTOList = viewsService.getViewsData();
        // 查询文章统计
        List<BlogStatisticsDTO> articleStatisticsList = blogDao.listArticleStatistics();
        // 查询分类数据
        List<TypeVo> typeList = typeDao.getTypeCount();
        // 查询标签数据
        List<Tag> tagList = tagDao.selectList(null);
        // 查询博客浏览量前五
        List<BlogRankDTO> blogRankDTOList = blogDao.selectList(new LambdaQueryWrapper<Blog>()
                .select(Blog::getBlogId, Blog::getTitle, Blog::getViews)
                .last("limit 5").orderByDesc(Blog::getViews))
                .stream().map(blog -> BlogRankDTO.builder()
                        .title(blog.getTitle())
                        .views(blog.getViews())
                        .build())
                .sorted(Comparator.comparingInt(BlogRankDTO::getViews).reversed())
                .collect(Collectors.toList());
        // 查询redis访问量前五的文章
//        Map<Object, Double> articleMap = redisService.zReverseRangeWithScore(ARTICLE_VIEWS_COUNT, 0, 4);
        BlogBackInfoDTO blogBackInfoDTO = BlogBackInfoDTO.builder()
                .articleStatisticsList(articleStatisticsList)
                .tagList(tagList)
                .viewsCount(viewsCount)
                .messageCount(messageCount)
                .userCount(userCount)
                .articleCount(articleCount)
                .blogRankDTOList(blogRankDTOList)
                .typeList(typeList)
                .viewsDTOList(viewsDTOList)
                .build();
        return blogBackInfoDTO;
    }

    @Override
    public Page<BlogBackDTO> adminBlogPage(QueryPageBean queryPageBean) {
        Page<BlogBackDTO> blogBackDTOPage = new Page<>();
        blogBackDTOPage.setRecords(blogDao.adminBlogPage(queryPageBean));
        Integer total = blogDao.selectCount(new LambdaQueryWrapper<Blog>().
                like(StringUtils.isNotBlank(queryPageBean.getQueryString()), Blog::getContent, queryPageBean.getQueryString()).
                or().like(StringUtils.isNotBlank(queryPageBean.getQueryString()), Blog::getTitle, queryPageBean.getQueryString()));
        blogBackDTOPage.setTotal(total);
        return blogBackDTOPage;
    }

    @Cacheable(value = {"AdminBlog"}, key = "#uid")
    public Page<BlogVo> findPage(QueryPageBean queryPageBean, Long uid) {
        currentPage = queryPageBean.getCurrentPage();
        pageSize = queryPageBean.getPageSize();
        start = (currentPage - 1) * pageSize;

        //设置分页条件
        Page<BlogVo> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid);
        //执行全部查询
        page.setRecords(blogDao.getAllBlogs(uid, start, pageSize));
        //查询总记录数
        page.setTotal(blogDao.selectCount(wrapper));
        return page;
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "AdminBlog", key = "#uid"),
                    @CacheEvict(value = {"BlogPage"}, allEntries = true)
            })
    public boolean addBlog(AddBlogVo addBlogVo, Long uid) {
        Blog blog = new Blog();
        BeanUtils.copyProperties(addBlogVo, blog);
        blog.setUid(uid);
        Long blogId = IdWorker.getId(Blog.class);
        blog.setBlogId(blogId);
        blog.setFirstPicture(isImagesTrue(blog.getFirstPicture()));
        blogDao.insert(blog);
        // 还要插入标签与博客的中间表
        blogTagService.addOneBlogTag(blogId, addBlogVo.getValue());
        return true;
    }

    /**
     * 添加或删除博客后，每页显示的博客都会发生变化，整个分页map都需要更新，所以得删除
     *
     * @param queryPageBean
     * @return page
     */
    @Transactional
    @Cacheable(value = {"BlogPage"}, key = "#root.methodName+'['+#queryPageBean.currentPage+']'", condition = "#queryPageBean.queryString==null")
    public Page<BlogVo> findHomePage(QueryPageBean queryPageBean) {
        //设置分页条件
        Page<BlogVo> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.like(queryPageBean.getQueryString() != null, "content", queryPageBean.getQueryString());
        page.setTotal(blogDao.selectCount(wrapper));
        page.setRecords(blogDao.findHomePage(queryPageBean));
        return page;
    }

    @Transactional
    @Cacheable(value = {"BlogMap"}, key = "#blog_id")
    public BlogVo getOneBlog(Long blog_id) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_id", blog_id);
        // 返回的博客要含有昵称，所以要用vo
        Blog blog = blogDao.selectOne(wrapper);
        BlogVo blogVo = new BlogVo();
        User user = userService.getById(blog.getUid());
        BeanUtils.copyProperties(blog, blogVo);
        String content = blog.getContent();
        blogVo.setNickname(user.getNickname());
        blogVo.setAvatar(user.getAvatar());
        if (content != null) {
            blogVo.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        }   // 将博客对象中的正文内容markdown格式的文本转换成html元素格式
        return blogVo;
    }

    @Cacheable(value = {"BlogPage"}, key = "#root.methodName")
    public List<Blog> getLatestList() {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.select("blog_id", "title", "create_time")
                .last("limit 0,7")
                .orderByDesc("create_time");
        return blogDao.selectList(wrapper);
    }

    @Cacheable(value = {"BlogPage"}, key = "#root.methodName+'['+#queryPageBean.typeId+']'")
    public Page<BlogVo> getByTypeId(QueryPageBean queryPageBean) {
        //设置分页条件
        Page<BlogVo> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq(queryPageBean.getTypeId() != null, "type_id", queryPageBean.getTypeId());
        page.setTotal(blogDao.selectCount(wrapper));
        page.setRecords(blogDao.findHomePage(queryPageBean));
        return page;
    }

    @Override
    public void setViews(Long blogId) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_id", blogId);
        Blog blog = blogDao.selectOne(wrapper);
        update(new UpdateWrapper<Blog>()
                .set("views", blog.getViews() + 1)
                .eq("blog_id", blogId));
    }

    @Override
    public boolean thumbsUp(Long blogId, Long uid) {
        QueryWrapper<ThumbsUp> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_id", blogId)
                .eq("uid", uid);

        if (thumbsUpDao.selectCount(wrapper) != 0) { // 该用户已点赞过该篇博客
            thumbsUpDao.delete(wrapper);
            return false;
        }

        ThumbsUp thumbsUp = new ThumbsUp();
        thumbsUp.setBlogId(blogId);
        thumbsUp.setUid(uid);
        thumbsUpDao.insert(thumbsUp);
        return true;
    }

    public Page<BlogVo> search(QueryPageBean queryPageBean) {
        //设置分页条件
        Page<BlogVo> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.like("content", queryPageBean.getQueryString());
        page.setTotal(blogDao.selectCount(wrapper));
        page.setRecords(blogDao.findHomePage(queryPageBean));
        return page;
    }

    /**
     * 用户提供的图片链接无效就自动生成图片
     *
     * @param postUrl 首图url
     * @return 图片url
     */
    public String isImagesTrue(String postUrl) {
        if (postUrl.contains("tcefrep.oss-cn-beijing.aliyuncs.com")) {   //本人的oss地址，就无需检验图片有效性
            return postUrl;
        }
        int max = 1000;
        int min = 1;
        String picUrl = "https://unsplash.it/800/450?image=";
        try {
            URL url = new URL(postUrl);
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setRequestMethod("POST");
            urlCon.setRequestProperty("Content-type",
                    "application/x-www-form-urlencoded");
            if (urlCon.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return postUrl;
            } else {
                Random random = new Random();
                int s = random.nextInt(max) % (max - min + 1) + min;
                return picUrl + s;
            }
        } catch (Exception e) {   // 代表图片链接无效
            Random random = new Random();
            int s = random.nextInt(max) % (max - min + 1) + min;
            return picUrl + s;
        }
    }

}
