package pers.fjl.common.constant;

/**
 * redis常量
 *
 */
public class RedisConst {

    /**
     * 验证码过期时间
     */
    public static final long CODE_EXPIRE_TIME = 30 * 60;

    /**
     * 用户地区分布更新时间
     */
    public static final Integer USER_AREA_EXPIRE_TIME = 60 * 60;

    /**
     * 验证码
     */
    public static final String USER_CODE_KEY = "code:";

    /**
     * 用户权限列表
     */
    public static final String USER_RESOURCE_KEY = "UserResource::";

    /**
     * 有效的token列表
     */
    public static final String TOKEN_ALLOW_LIST = "token:";

    public static final Integer HOUR = 3600;

    /**
     * 博客浏览量
     */
    public static final String BLOG_VIEWS_COUNT = "blog_views_count";

    /**
     * 文章点赞量
     */
    public static final String ARTICLE_LIKE_COUNT = "article_like_count";

    /**
     * 用户点赞文章
     */
    public static final String ARTICLE_USER_LIKE = "article_user_like:";

    /**
     * 评论点赞量
     */
    public static final String COMMENT_LIKE_COUNT = "comment_like_count";

    /**
     * 用户点赞评论
     */
    public static final String COMMENT_USER_LIKE = "comment_user_like:";

    /**
     * 网站配置
     */
    public static final String WEBSITE_CONFIG = "website_config";

    /**
     * 用户地区
     */
    public static final String USER_AREA = "user_area";

    /**
     * 访客地区
     */
    public static final String VISITOR_AREA = "visitor_area";

    /**
     * 页面封面
     */
    public static final String PAGE_COVER = "page_cover";

    /**
     * 关于我信息
     */
    public static final String ABOUT = "about";

    /**
     * 访客
     */
    public static final String UNIQUE_VISITOR = "unique_visitor";

}
