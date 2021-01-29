package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.po.User;


/**
 * <p>
 *  用户服务类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-26
 */
public interface UserService extends IService<User> {
    /**
     * 利用jwt登录验证
     *
     * @param user
     * @return
     */
    User login(User user);

    /**
     * 查询用户是否存在
     *
     * @param username
     * @return
     */
    boolean UserExist(String username);

    /**
     * 根据用户id查询用户
     *
     * @param userId
     * @return user
     */
    User findById(String userId);
}
