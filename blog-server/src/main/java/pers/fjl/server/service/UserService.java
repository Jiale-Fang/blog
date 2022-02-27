package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.dto.UserAreaDTO;
import pers.fjl.common.dto.UserBackDTO;
import pers.fjl.common.dto.UserOnlineDTO;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.User;
import pers.fjl.server.dto.UserDetailDTO;

import javax.servlet.http.HttpServletRequest;
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
     * @param user user
     * @return boolean
     */
    boolean add(User user);

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
     * @return userList
     */
    List<User> getUserList();

    /**
     * 获取用户分布地区
     * @return list
     */
    List<UserAreaDTO> listUserAreas();

    void statisticalUserArea();

    User selectByUsername(String username);

    /**
     * 获取后台用户列表
     * @param queryPageBean 分页实体
     * @return page
     */
    Page<UserBackDTO> adminUser(QueryPageBean queryPageBean);

    /**
     * 获取在线用户列表
     * @param queryPageBean 分页实体
     * @return page
     */
    Page<UserOnlineDTO> listOnlineUsers(QueryPageBean queryPageBean);

    /**
     * 获取用户信息
     * @param username
     * @param request
     * @param ipAddress
     * @param ipSource
     * @return
     */
    UserDetailDTO getUserDetail(String username, HttpServletRequest request, String ipAddress, String ipSource);
}
