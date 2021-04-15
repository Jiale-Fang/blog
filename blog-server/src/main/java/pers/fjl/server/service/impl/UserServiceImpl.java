package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.po.User;
import pers.fjl.server.dao.UserDao;
import pers.fjl.server.service.UserService;
import pers.fjl.server.utils.BeanUtilsIgnoreNull;
import pers.fjl.server.utils.RedisUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

    @CacheEvict(value = {"UserListMap"})
    public void add(User user) {
        log.info("addUser.user.getUsername():[{}]", user.getUsername());
        log.info("addUser.user.getPassword():[{}]", user.getPassword());
        if (userService.UserExist(user.getUsername())) {
            throw new RuntimeException("用户名已被注册");
        }
        user.setDataStatus(MessageConstant.UserAble);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAvatar(isImagesTrue(user.getAvatar()));
        userDao.insert(user);
    }

    /**
     * 用户提供的图片链接无效就自动生成图片
     * @param postUrl
     * @return
     */
    public String isImagesTrue(String postUrl) {
        int max = 1000;
        int min = 1;
        String picUrl = "https://unsplash.it/100/100?image=";
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
                return picUrl+s;
            }
        } catch (Exception e) {   // 代表图片链接无效
            Random random = new Random();
            int s = random.nextInt(max) % (max - min + 1) + min;
            return picUrl+s;
        }
    }

    @Override
    public boolean verifyCode(String verKey, String code, String realCode) throws RuntimeException {
        redisUtil.del(verKey);  // 验证码是否正确都删除，否则验证错误的验证码会存在redis中无法删除
        if (realCode == null || StringUtils.isEmpty(realCode)) {
            throw new RuntimeException("请输入验证码！");
        }
        if (!code.equalsIgnoreCase(realCode)) {
            throw new RuntimeException("请输入正确的验证码！");
        }
        return true;
    }

    @Override
    public boolean updateUser(User user) {
        User userDB = userDao.selectById(user.getUid());
        if (userService.UserExist(user.getUsername()) && !userDB.getUsername().equals(user.getUsername())) {
            throw new RuntimeException("用户名已被注册");
        }
        if (user.getPassword() != null && !user.getPassword().equals("")) { // 用户更改了密码
            System.out.println("修改密码");
            user.setPassword(encoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        BeanUtilsIgnoreNull.copyPropertiesIgnoreNull(user, userDB);
        userDB.setUpdateTime(LocalDateTime.now());
        userDB.setDataStatus(MessageConstant.UserAble);
        userDB.setAvatar(isImagesTrue(user.getAvatar()));
        userDao.updateById(userDB);
        return true;
    }

    @Cacheable(value = {"UserListMap"})
    public List<User> getUserList() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("uid", "username", "nickname", "avatar")
                .orderByAsc("create_time");
        return userDao.selectList(wrapper);
    }

    @Override
    public User selectByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .select("username", "nickname", "avatar", "uid");
        return userDao.selectOne(wrapper);
    }

    public User login(User user) {
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
