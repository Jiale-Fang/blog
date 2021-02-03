package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.po.User;
import pers.fjl.server.dao.UserDao;
import pers.fjl.server.service.UserService;
import pers.fjl.server.utils.RedisUtil;

import javax.annotation.Resource;

/**
 * <p>
 * 用户服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-26
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Resource
    private UserService userService;
    @Resource
    private UserDao userDao;
    @Resource
    private BCryptPasswordEncoder encoder;
    @Resource
    private RedisUtil redisUtil;

    public boolean UserExist(String username) {//搜索用户名是否存在
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("1");
        wrapper.eq("username", username).last("limit 1");
        return userDao.selectCount(wrapper) != 0;
    }

    @Cacheable(value = {"UserMap"}, key = "#userId")
    public User findById(Long userId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", userId);
        if (userDao.selectOne(wrapper) == null) {
            return null;
        }
        return userDao.selectOne(wrapper);
    }

    @Override
    public void add(User user) {
        log.info("addUser.user.getUsername():[{}]", user.getUsername());
        log.info("addUser.user.getPassword():[{}]", user.getPassword());
        if (userService.UserExist(user.getUsername())) {
            throw new RuntimeException("用户名已被注册");
        }
        user.setDataStatus(MessageConstant.UserAble);
        user.setPassword(encoder.encode(user.getPassword()));
        userDao.insert(user);
    }

    @Override
    public boolean verifyCode(String verKey, String code, String realCode) {
        redisUtil.del(verKey);  // 验证码是否正确都删除，否则验证错误的验证码会存在redis中无法删除
        if (realCode == null || StringUtils.isEmpty(realCode)) {
            throw new RuntimeException("请输入验证码！");
        }
        if (!code.equalsIgnoreCase(realCode)) {
            throw new RuntimeException("请输入正确的验证码！");
        }
        return true;
    }

    public User login(User user) throws RuntimeException {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("uid", "username", "password", "data_status", "nickname", "avatar");
        wrapper.eq("username", user.getUsername());
        //登录的用户
        User login_user = userDao.selectOne(wrapper);
        log.debug("login_user:[{}]", login_user.toString());
        if (!encoder.matches(user.getPassword(), login_user.getPassword())) {
            throw new RuntimeException("用户名或密码不正确，登录失败");
        }
        if (login_user.isDataStatus() == (MessageConstant.UserDisable)) {
            throw new RuntimeException("用户已被禁用,登录失败");
        }
        update(new UpdateWrapper<User>()
//               .set("last_time", new Date())
                .set("last_ip", user.getLastIp())
                .eq("username", login_user.getUsername()));

        return login_user;
    }
}
