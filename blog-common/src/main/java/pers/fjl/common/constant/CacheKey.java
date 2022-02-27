package pers.fjl.common.constant;

/**
 * 用于同一管理和生成缓存的region和key， 避免多个项目使用的key重复
 */
public interface CacheKey {
    /**
     * 验证码 前缀
     * 完整key: captcha:{key} -> str
     */
    String CAPTCHA = "captcha";
    /**
     * 资源 前缀
     * 完整key: resource:{resourceId} -> obj
     */
    String RESOURCE = "resource";

    /**
     * 用户拥有的资源 前缀
     * 完整key: user_resource:{userId} -> [RESOURCE_ID, ...]
     */
    String USER_RESOURCE = "user_resource";

    /**
     * 所有需要鉴权的资源
     */
    String RESOURCE_NEED_TO_CHECK = "resource_need_to_check";
}
