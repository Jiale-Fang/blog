package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.server.dto.UserDetailDTO;
import pers.fjl.common.po.User;
import pers.fjl.server.dao.UserDao;
import pers.fjl.server.dao.admin.RoleDao;
import pers.fjl.server.exception.BizException;
import pers.fjl.server.utils.IpUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;


/**
 * 用户信息服务实现类
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserDao userDao;
    @Resource
    private RoleDao roleDao;
    @Resource
    private HttpServletRequest request;

    /**
     * 返回接口所需的UserDetails实体
     *
     * @param username 用户名
     * @return UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new BizException(MessageConstant.USERNAME_IS_NULL);
        }
        // 查询账号是否存在
        User user = userDao.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
        if (Objects.isNull(user)) {
            throw new BizException(MessageConstant.USER_NOT_EXIST);
        }
        // 封装登录信息
        return convertUserDetail(user, request);
    }

    /**
     * 封装用户登录信息
     *
     * @param user    用户账号
     * @param request 请求
     * @return 用户登录信息
     */
    public UserDetailDTO convertUserDetail(User user, HttpServletRequest request) {
        // 查询账号角色
        List<String> roleList = roleDao.listRolesByUid(user.getUid());
//        // 查询账号点赞信息
//        Set<Object> articleLikeSet = redisService.sMembers(ARTICLE_USER_LIKE + userInfo.getId());
//        Set<Object> commentLikeSet = redisService.sMembers(COMMENT_USER_LIKE + userInfo.getId());
        // 获取设备信息
        String ipAddress = IpUtils.getIpAddr(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        UserAgent userAgent = IpUtils.getUserAgent(request);
        // 封装权限集合
        return UserDetailDTO.builder()
                .uid(user.getUid())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .roleList(roleList)
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .lastIp(ipAddress)
                .loginType(user.getLoginType())
                .ipSource(ipSource)
                .status(user.isStatus())
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .lastLoginTime(LocalDateTime.now())
                .build();
    }

}
