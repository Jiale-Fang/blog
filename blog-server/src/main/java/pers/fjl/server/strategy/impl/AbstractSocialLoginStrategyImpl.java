package pers.fjl.server.strategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.sun.jersey.api.client.filter.LoggingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import pers.fjl.common.constant.CommonConst;
import pers.fjl.common.dto.SocialTokenDTO;
import pers.fjl.common.dto.SocialUserInfoDTO;
import pers.fjl.common.dto.UserInfoDTO;
import pers.fjl.common.po.User;
import pers.fjl.common.po.admin.UserRole;
import pers.fjl.server.dao.UserDao;
import pers.fjl.server.dao.admin.TbUserRoleDao;
import pers.fjl.server.dto.UserDetailDTO;
import pers.fjl.server.exception.BizException;
import pers.fjl.server.service.UserService;
import pers.fjl.server.service.impl.UserDetailsServiceImpl;
import pers.fjl.server.strategy.SocialLoginStrategy;
import pers.fjl.server.utils.BeanCopyUtils;
import pers.fjl.server.utils.IpUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import static pers.fjl.common.constant.MessageConstant.USER_ABLE;


/**
 * 第三方登录抽象模板
 */
@Service
public abstract class AbstractSocialLoginStrategyImpl implements SocialLoginStrategy {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;
    @Autowired
    private TbUserRoleDao tbUserRoleDao;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private SessionRegistry sessionRegistry;

    @Override
    public UserInfoDTO login(String data) {
        // 创建登录信息
        UserDetailDTO userDetailDTO;
        // 获取第三方token信息
        SocialTokenDTO socialToken = getSocialToken(data);
        // 获取用户ip信息
        String ipAddress = IpUtils.getIpAddr(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        // 判断是否已注册
        User user = getUser(socialToken);
        if (Objects.nonNull(user)) {
            // 返回数据库用户信息
            userDetailDTO = userService.getUserDetail(user, ipAddress, ipSource);
        } else {
            // 获取第三方用户信息，保存到数据库返回
            userDetailDTO = saveUserDetail(socialToken, ipAddress, ipSource);
        }
        // 判断账号是否禁用
        if (!userDetailDTO.isStatus()) {
            throw new BizException("账号已被禁用");
        }
        // 将登录信息放入springSecurity管理
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetailDTO, null, userDetailDTO.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        //  用户名密码验证通过后，将含有用户信息的token注册session
        sessionRegistry.registerNewSession(request.getSession().getId(), auth.getPrincipal());
        // 返回用户信息
        return BeanCopyUtils.copyObject(userDetailDTO, UserInfoDTO.class);
    }

    /**
     * 获取第三方token信息
     *
     * @param data 数据
     * @return {@link SocialTokenDTO} 第三方token信息
     */
    public abstract SocialTokenDTO getSocialToken(String data);

    /**
     * 获取第三方用户信息
     *
     * @param socialTokenDTO 第三方token信息
     * @return {@link SocialUserInfoDTO} 第三方用户信息
     */
    public abstract SocialUserInfoDTO getSocialUserInfo(SocialTokenDTO socialTokenDTO);

    /**
     * 获取用户账号
     *
     * @return {@link User} 用户账号
     */
    private User getUser(SocialTokenDTO socialTokenDTO) {
        return userDao.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, socialTokenDTO.getOpenId())
                .eq(User::getLoginType, socialTokenDTO.getLoginType()));
    }

    /**
     * 新增用户信息
     *
     * @param socialToken token信息
     * @param ipAddress   ip地址
     * @param ipSource    ip源
     * @return {@link UserDetailDTO} 用户信息
     */
    private UserDetailDTO saveUserDetail(SocialTokenDTO socialToken, String ipAddress, String ipSource) {
        // 获取第三方用户信息
        SocialUserInfoDTO socialUserInfo = getSocialUserInfo(socialToken);
        // 保存用户信息
        User user = User.builder()
                .nickname(socialUserInfo.getNickname())
                .avatar(socialUserInfo.getAvatar())
                .uid(IdWorker.getId(User.class))
                .username(socialToken.getOpenId())
                .password(socialToken.getAccessToken())
                .loginType(socialToken.getLoginType())
                .lastLoginTime(LocalDateTime.now())
                .lastIp(ipAddress)
                .status(USER_ABLE)
                .ipSource(ipSource)
                .build();
        userDao.insert(user);
        UserRole userRole = UserRole.builder()
                .uid(user.getUid())
                .rid(2)
                .build();
        tbUserRoleDao.insert(userRole);
        return userDetailsService.convertUserDetail(user, request);
    }

}
