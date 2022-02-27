package pers.fjl.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pers.fjl.common.constant.CommonConst;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.dto.UserAreaDTO;
import pers.fjl.common.dto.UserBackDTO;
import pers.fjl.common.dto.UserOnlineDTO;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.User;
import pers.fjl.server.dao.UserDao;
import pers.fjl.server.dao.admin.RoleDao;
import pers.fjl.server.dto.UserDetailDTO;
import pers.fjl.server.service.UserService;
import pers.fjl.server.utils.BeanCopyUtils;
import pers.fjl.server.utils.IpUtils;
import pers.fjl.server.utils.RedisUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static pers.fjl.common.constant.RedisConst.USER_AREA;

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
    private RoleDao roleDao;
    @Resource
    private BCryptPasswordEncoder encoder;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SessionRegistry sessionRegistry;

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

    @Transactional
    @CacheEvict(value = {"UserListMap"})
    public boolean add(User user) {
        log.info("addUser.user.getUsername():[{}]", user.getUsername());
        log.info("addUser.user.getPassword():[{}]", user.getPassword());
        if (userService.UserExist(user.getUsername())) {
            return false;
        }
        user.setDataStatus(MessageConstant.USER_ABLE);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAvatar(isImagesTrue(user.getAvatar()));
        userDao.insert(user);
        return true;
    }

    /**
     * 用户提供的图片链接无效就自动生成图片
     *
     * @param postUrl 用户传来的头像url
     * @return url
     */
    public String isImagesTrue(String postUrl) {
        if (postUrl.contains("tcefrep.oss-cn-beijing.aliyuncs.com")) {   //本人的oss地址，就无需检验图片有效性
            return postUrl;
        }
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
                return picUrl + s;
            }
        } catch (Exception e) {   // 代表图片链接无效
            Random random = new Random();
            int s = random.nextInt(max) % (max - min + 1) + min;
            return picUrl + s;
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

    @CacheEvict(value = {"UserMap"}, key = "#user.getUid()")
    public boolean updateUser(User user) {
        User userDB = userDao.selectById(user.getUid());
        if (userService.UserExist(user.getUsername()) && !userDB.getUsername().equals(user.getUsername())) {
            return false;
        }
        if (user.getPassword() != null && !user.getPassword().equals("")) { // 用户更改了密码
            System.out.println("修改密码");
            user.setPassword(encoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        BeanCopyUtils.copyPropertiesIgnoreNull(user, userDB);
        userDB.setUpdateTime(LocalDateTime.now());
        userDB.setDataStatus(MessageConstant.USER_ABLE);
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

    @Override
    public Page<UserBackDTO> adminUser(QueryPageBean queryPageBean) {
//        //先把数据查出来封装到user的page中，然后再赋予到UserBackDTO的page中返回
//        Page<User> userPage = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like(queryPageBean.getQueryString() != null, "nickname", queryPageBean.getQueryString());
//        Page<User> userResultPage = userDao.selectPage(userPage, wrapper);
//        List<UserBackDTO> userBackDTOList = BeanCopyUtils.copyList(userResultPage.getRecords(), UserBackDTO.class);
        Page<UserBackDTO> userBackDTOPage = new Page<>();
        userBackDTOPage.setTotal(userDao.selectCount(wrapper));
        userBackDTOPage.setRecords(userDao.adminUser(queryPageBean));
        return userBackDTOPage;
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
        if (login_user.isDataStatus() == (MessageConstant.USER_DISABLE)) {
            throw new RuntimeException("用户已被禁用,登录失败");
        }
        update(new UpdateWrapper<User>()
//               .set("last_time", new Date())
                .set("last_ip", user.getLastIp())
                .eq("username", login_user.getUsername()));

        return login_user;
    }

    /**
     * 统计用户地区
     */
    public void statisticalUserArea() {
        // 统计用户地域分布
        Map<String, Long> userAreaMap = userDao.selectList(new LambdaQueryWrapper<User>().select(User::getIpSource))
                .stream()
                .map(item -> {
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(item.getIpSource())) {
                        return item.getIpSource().substring(0, 2)
                                .replaceAll(CommonConst.PROVINCE, "")
                                .replaceAll(CommonConst.CITY, "");
                    }
                    return CommonConst.UNKNOWN;
                })
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()));
        // 转换格式
        List<UserAreaDTO> userAreaList = userAreaMap.entrySet().stream()
                .map(item -> UserAreaDTO.builder()
                        .name(item.getKey())
                        .value(item.getValue())
                        .build())
                .collect(Collectors.toList());
        redisUtil.set(USER_AREA, JSON.toJSONString(userAreaList));
    }

    @Override
    public List<UserAreaDTO> listUserAreas() {
        userService.statisticalUserArea();
        List<UserAreaDTO> userAreaDTOList = new ArrayList<>();
        // 查询注册用户区域分布
        Object userArea = redisUtil.get(USER_AREA);
        if (Objects.nonNull(userArea)) {
            userAreaDTOList = JSON.parseObject(userArea.toString(), List.class);
        }
        return userAreaDTOList;
    }

    @Override
    public Page<UserOnlineDTO> listOnlineUsers(QueryPageBean queryPageBean) {
        // 获取security在线session
        List<UserOnlineDTO> userOnlineDTOList = sessionRegistry.getAllPrincipals().stream()
                .filter(item -> sessionRegistry.getAllSessions(item, false).size() > 0)
                .map(item -> JSON.parseObject(JSON.toJSONString(item), UserOnlineDTO.class))
                .filter(item -> com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(queryPageBean.getQueryString()) || item.getNickname().contains(queryPageBean.getQueryString()))
                .sorted(Comparator.comparing(UserOnlineDTO::getLastLoginTime).reversed())
                .collect(Collectors.toList());

        Page<UserOnlineDTO> userOnlineDTOPage = new Page<>();
        // 执行分页
        int fromIndex = ((queryPageBean.getCurrentPage() - 1) * queryPageBean.getPageSize());
        int size = queryPageBean.getPageSize();
        int toIndex = userOnlineDTOList.size() - fromIndex > size ? fromIndex + size : userOnlineDTOList.size();
        List<UserOnlineDTO> userOnlineList = userOnlineDTOList.subList(fromIndex, toIndex);
        userOnlineDTOPage.setRecords(userOnlineList);
        userOnlineDTOPage.setTotal(userOnlineDTOList.size());
        return userOnlineDTOPage;
    }

    @Override
    public UserDetailDTO getUserDetail(String username, HttpServletRequest request, String ipAddress, String ipSource) {
        User user = userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        // 查询账号角色
        List<String> roleList = roleDao.listRolesByUid(user.getUid());
        UserAgent userAgent = IpUtils.getUserAgent(request);
        return UserDetailDTO.builder()
                .uid(user.getUid())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .roleList(roleList)
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .lastIp(ipAddress)
                .ipSource(ipSource)
                .dataStatus(user.isDataStatus())
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .lastLoginTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")))
                .build();
    }

    //    @Override
    public void removeOnlineUser(Integer userInfoId) {
        // 获取用户session
        List<Object> userInfoList = sessionRegistry.getAllPrincipals().stream().filter(item -> {
            UserDetailDTO userDetailDTO = (UserDetailDTO) item;
            return userDetailDTO.getUid().equals(userInfoId);
        }).collect(Collectors.toList());
        List<SessionInformation> allSessions = new ArrayList<>();
        userInfoList.forEach(item -> allSessions.addAll(sessionRegistry.getAllSessions(item, false)));
        // 注销session
        allSessions.forEach(SessionInformation::expireNow);
    }

}
