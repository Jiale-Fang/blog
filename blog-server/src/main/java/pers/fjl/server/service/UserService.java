package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.po.User;

import java.util.List;


/**
 * <p>
 * 用户服务类
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
     * @return user
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
    User findById(Long userId);

    /**
     * 用户注册
     *
     * @param user
     */
    void add(User user);

    /**
     * 检验验证码
     *
     * @return boolean
     */
    boolean verifyCode(String verKey, String captcha, String sessionCode);

    /**
     * 更新用户信息
     * @param user
     * @return boolean
     */
    boolean updateUser(User user);

    /**
     * 获取本站用户列表
     * @return
     */
    List<User> getUserList();

    User selectByUsername(String username);
}
