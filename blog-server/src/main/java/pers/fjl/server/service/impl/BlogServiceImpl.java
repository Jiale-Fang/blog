package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.fjl.common.dto.*;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.*;
import pers.fjl.common.vo.AddBlogVO;
import pers.fjl.common.vo.BlogVO;
import pers.fjl.common.vo.TypeVO;
import pers.fjl.server.dao.*;
import pers.fjl.server.filter.SensitiveFilter;
import pers.fjl.server.service.*;
import pers.fjl.server.utils.BeanCopyUtils;
import pers.fjl.server.utils.MarkdownUtils;
import pers.fjl.server.utils.RedisUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static pers.fjl.common.constant.CommonConst.BLOG_SET;
import static pers.fjl.common.constant.RedisConst.BLOG_VIEWS_COUNT;

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
    private CommentDao commentDao;
    @Resource
    private BlogTagService blogTagService;
    @Resource
    private UserService userService;
    @Resource
    private BlogService blogService;
    @Resource
    private TagService tagService;
    @Resource
    private ViewsService viewsService;
    @Resource
    private ThumbsUpDao thumbsUpDao;
    @Resource
    private FavoritesDao favoritesDao;
    @Resource
    private MessageDao messageDao;
    @Resource
    private BlogTagDao blogTagDao;
    @Resource
    private ViewsDao viewsDao;
    @Resource
    private UserDao userDao;
    @Resource
    private TypeDao typeDao;
    @Resource
    private TagDao tagDao;
    @Autowired
    private HttpSession session;
    @Resource
    private RedisUtil redisUtil;

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
        Integer viewsCount = viewsDao.selectOne(new QueryWrapper<Views>().select("SUM(count) as count")).getCount();
        // 查询留言量
        Integer messageCount = messageDao.selectCount(null);
        // 查询用户量
        Integer userCount = userDao.selectCount(null);
        // 查询文章量
        Integer blogCount = blogDao.selectCount(null);
        // 查询一周访问量
        List<ViewsDTO> viewsDTOList = viewsService.getViewsData();
        // 查询文章统计
        List<BlogStatisticsDTO> blogStatisticsList = blogDao.listArticleStatistics();
        // 查询分类数据
        List<TypeVO> typeList = typeDao.getTypeCount();
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
//        Map<Object, Double> blogMap = redisService.zReverseRangeWithScore(blog_VIEWS_COUNT, 0, 4);
        BlogBackInfoDTO blogBackInfoDTO = BlogBackInfoDTO.builder()
                .articleStatisticsList(blogStatisticsList)
                .tagList(tagList)
                .viewsCount(viewsCount)
                .messageCount(messageCount)
                .userCount(userCount)
                .articleCount(blogCount)
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
        blogBackDTOPage.setTotal(blogDao.adminBlogPageCount(queryPageBean));
        return blogBackDTOPage;
    }

    @Caching(
            evict = {
                    @CacheEvict(value = {"BlogPage"}, allEntries = true),
                    @CacheEvict(value = {"BlogMap"}, allEntries = true)
            })
    @Override
    @Transactional
    public void adminSaveOrUpdateBlog(BlogVO blogVO, Long uid) {
        // 保存文章分类
        Type type = saveType(blogVO);
        // 修改文章
        Blog blog = BeanCopyUtils.copyObject(blogVO, Blog.class);
        if (Objects.nonNull(type)) {
            blog.setTypeId(type.getTypeId());
        }
        blog.setUid(uid);
        blog.setContent(SensitiveFilter.filter(blog.getContent()));
        blogService.saveOrUpdate(blog);
        // 保存文章标签
        saveBlogTag(blogVO, blog.getBlogId());
    }

    /**
     * 保存文章分类
     *
     * @param BlogVO 文章信息
     * @return {@link Type} 文章分类
     */
    private Type saveType(BlogVO blogVO) {
        // 判断分类是否存在
        Type type = typeDao.selectOne(new LambdaQueryWrapper<Type>()
                .eq(Type::getTypeName, blogVO.getTypeName()));
        if (Objects.isNull(type)) {
            type = type.builder()
                    .typeName(blogVO.getTypeName())
                    .build();
            typeDao.insert(type);
        }
        return type;
    }

    /**
     * 保存文章标签
     *
     * @param blogVO 文章信息
     */
    private void saveBlogTag(BlogVO blogVO, Long blogId) {
        // 编辑文章则先删除文章所有标签
        if (Objects.nonNull(blogVO.getBlogId())) {
            blogTagDao.delete(new LambdaQueryWrapper<BlogTag>()
                    .eq(BlogTag::getBlogId, blogVO.getBlogId()));
        }
        // 添加文章标签
        List<String> tagNameList = blogVO.getTagNameList();
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            // 查询已存在的标签
            List<Tag> existTagList = tagService.list(new LambdaQueryWrapper<Tag>()
                    .in(Tag::getTagName, tagNameList));
            List<String> existTagNameList = existTagList.stream()
                    .map(Tag::getTagName)
                    .collect(Collectors.toList());
            List<Integer> existTagIdList = existTagList.stream()
                    .map(Tag::getTagId)
                    .collect(Collectors.toList());
            // 对比新增不存在的标签（去掉存在的就是不存在的，即我们要新增的）
            tagNameList.removeAll(existTagNameList);
            if (CollectionUtils.isNotEmpty(tagNameList)) {
                List<Tag> tagList = tagNameList.stream().map(item -> Tag.builder()
                        .tagName(item)
                        .build())
                        .collect(Collectors.toList());
                tagService.saveBatch(tagList);
                List<Integer> tagIdList = tagList.stream()
                        .map(Tag::getTagId)
                        .collect(Collectors.toList());
                existTagIdList.addAll(tagIdList);
            }
            // 提取标签id绑定文章
            List<BlogTag> blogTagList = existTagIdList.stream().map(item -> BlogTag.builder()
                    .blogId(blogId)
                    .tagId(item)
                    .build())
                    .collect(Collectors.toList());
            blogTagService.saveBatch(blogTagList);
        }
    }

    public Page<BlogVO> findPage(QueryPageBean queryPageBean, Long uid) {
        currentPage = queryPageBean.getCurrentPage();
        pageSize = queryPageBean.getPageSize();
        start = (currentPage - 1) * pageSize;

        //设置分页条件
        Page<BlogVO> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid);
        //执行全部查询
        page.setRecords(blogDao.getAllBlogs(uid, start, pageSize));
        //查询总记录数
        page.setTotal(blogDao.selectCount(wrapper));
        return page;
    }

    @Transactional(rollbackFor = Exception.class)
    @Caching(
            evict = {
                    @CacheEvict(value = {"BlogPage"}, allEntries = true),
                    @CacheEvict(value = {"BlogMap"}, condition = "#addBlogVO.blogId!=null", key = "#addBlogVO.blogId"),
            })
    public Long addOrUpdateBlog(AddBlogVO addBlogVO, Long uid) {
        Blog blog = new Blog();
        BeanUtils.copyProperties(addBlogVO, blog);
        blog.setUid(uid);
        if (addBlogVO.getBlogId() == null || Objects.isNull(addBlogVO.getBlogId())) {   //代表是添加博客
            Long blogId = IdWorker.getId(Blog.class);
            blog.setBlogId(blogId);
        } else {    //更新博客
            blogTagService.remove(new LambdaQueryWrapper<BlogTag>().eq(BlogTag::getBlogId, blog.getBlogId()));
        }
        blog.setFirstPicture(isImagesTrue(blog.getFirstPicture()));
        blog.setContent(SensitiveFilter.filter(blog.getContent()));
        blogService.saveOrUpdate(blog);
        // 还要插入标签与博客的中间表
        blogTagService.addOneBlogTag(blog.getBlogId(), addBlogVO.getValue());
        return blog.getBlogId();
    }

    /**
     * 添加或删除博客后，每页显示的博客都会发生变化，整个分页map都需要更新，所以得删除
     *
     * @param queryPageBean 分页实体
     * @return page
     */
    @Transactional
    @Cacheable(value = {"BlogPage"}, key = "#root.methodName+'['+#queryPageBean.currentPage+']'+'['+#queryPageBean.pageSize+']'", condition = "#queryPageBean.queryString==null")
    public Page<BlogVO> findHomePage(QueryPageBean queryPageBean) {
        //设置分页条件
        Page<BlogVO> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.like(queryPageBean.getQueryString() != null, "content", queryPageBean.getQueryString());
        page.setTotal(blogDao.selectCount(wrapper));
        page.setRecords(blogDao.findHomePage(queryPageBean));
        return page;
    }

    @Transactional
    @Cacheable(value = {"BlogMap"}, key = "#blogId")
    public BlogVO getOneBlog(Long blogId) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_id", blogId);
        // 返回的博客要含有昵称，所以要用vo
        Blog blog = blogDao.selectOne(wrapper);
        BlogVO blogVO = new BlogVO();
        User user = userService.getById(blog.getUid());
        BeanUtils.copyProperties(blog, blogVO);
        String content = blog.getContent();
        blogVO.setNickname(user.getNickname());
        blogVO.setAvatar(user.getAvatar());
        blogVO.setTagNameList(tagDao.getBlogTagList(blogId).stream().map(Tag::getTagName).collect(Collectors.toList()));
        if (content != null) {
            blogVO.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        }   // 将博客对象中的正文内容markdown格式的文本转换成html元素格式
        return blogVO;
    }

    /**
     * 更新博客浏览量,在当前页面连接里，同一篇文章第二次访问就不再添加浏览量
     *
     * @param blogId 博客id
     */
    @Async
    public void updateBlogViewsCount(Long blogId) {
        // 判断是否第一次访问，增加浏览量
        Set<Long> blogSet = (Set<Long>) Optional.ofNullable(session.getAttribute(BLOG_SET)).orElse(new HashSet<>());
        if (!blogSet.contains(blogId)) {    // 代表是第一次
            blogSet.add(blogId);
            session.setAttribute(BLOG_SET, blogSet);
            // 浏览量+1
            redisUtil.zIncr(BLOG_VIEWS_COUNT, blogId, 1D);
            setBlogViews(blogId);
        }
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
    public Page<BlogVO> getByTypeId(QueryPageBean queryPageBean) {
        //设置分页条件
        Page<BlogVO> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq(queryPageBean.getTypeId() != null, "type_id", queryPageBean.getTypeId());
        page.setTotal(blogDao.selectCount(wrapper));
        page.setRecords(blogDao.findHomePage(queryPageBean));
        return page;
    }

    public void setBlogViews(Long blogId) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.select("views")
                .eq("blog_id", blogId);
        Blog blog = blogDao.selectOne(wrapper);
        update(new UpdateWrapper<Blog>()
                .set("views", blog.getViews() + 1)
                .eq("blog_id", blogId));
    }

    @Override
    @Transactional
    public boolean thumbsUp(Long blogId, Long uid) {
        QueryWrapper<ThumbsUp> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_id", blogId)
                .eq("uid", uid);
        Blog blogDB = blogDao.selectById(blogId);
        if (thumbsUpDao.selectCount(wrapper) != 0) { // 该用户已点赞过该篇博客
            thumbsUpDao.delete(wrapper);
            blogDB.setThumbs(blogDB.getThumbs() - 1);
            blogDao.updateById(blogDB);
            return false;
        }
        ThumbsUp thumbsUp = new ThumbsUp();
        thumbsUp.setBlogId(blogId);
        thumbsUp.setUid(uid);
        thumbsUpDao.insert(thumbsUp);
        blogDB.setThumbs(blogDB.getThumbs() + 1);
        blogDao.updateById(blogDB);
        return true;
    }

    @Override
    @Transactional
    public boolean favorite(Long blogId, Long uid) {
        QueryWrapper<Favorites> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_id", blogId)
                .eq("uid", uid);
        Integer count = favoritesDao.selectCount(wrapper);
        if (count != 0) {  //代表已经收藏了该文章
            favoritesDao.delete(wrapper);
            return false;
        }
        Favorites favorites = new Favorites();
        favorites.setBlogId(blogId);
        favorites.setUid(uid);
        favoritesDao.insert(favorites);
        return true;
    }

    @Override
    public BlogInfoDTO blogInfo() {
        BlogInfoDTO blogInfoDTO = new BlogInfoDTO();
        blogInfoDTO.setBlogCount(blogDao.selectCount(null));
        blogInfoDTO.setTagCount(tagDao.selectCount(null));
        blogInfoDTO.setTypeCount(typeDao.selectCount(null));
        return blogInfoDTO;
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

    @Override
    public Page<Blog> search(QueryPageBean queryPageBean) {
        Page<Blog> blogPage = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        return blogDao.selectPage(blogPage, new LambdaQueryWrapper<Blog>()
                .like(queryPageBean.getQueryString() != null, Blog::getTitle, queryPageBean.getQueryString())
                .or().like(queryPageBean.getQueryString() != null, Blog::getContent, queryPageBean.getQueryString())
                .orderByDesc(Blog::getViews));
    }

    @Override
    public Page<BlogVO> findFavoritesPage(QueryPageBean queryPageBean, Long uid) {
        Page<BlogVO> blogVOPage = new Page<BlogVO>();
        blogVOPage.setRecords(blogDao.findFavoritesPage(queryPageBean, uid));
        return blogVOPage;
    }

    @Caching(
            evict = {
                    @CacheEvict(value = {"BlogPage"}, allEntries = true),
                    @CacheEvict(value = {"BlogMap"}, allEntries = true)
            })
    @Override
    @Transactional
    public void deleteBlogs(List<Long> blogIdList) {
        blogTagDao.delete(new LambdaQueryWrapper<BlogTag>() //删除博客标签的中间表数据
                .in(BlogTag::getBlogId, blogIdList));
        // 删除博客的点赞和收藏信息
        thumbsUpDao.delete(new LambdaQueryWrapper<ThumbsUp>().in(ThumbsUp::getBlogId, blogIdList));
        favoritesDao.delete(new LambdaQueryWrapper<Favorites>().in(Favorites::getBlogId, blogIdList));
        commentDao.delete(new LambdaQueryWrapper<Comment>().in(Comment::getBlogId, blogIdList));// 删除博客下的所有评论数据
        blogDao.deleteBatchIds(blogIdList); //删除博客
    }


}
