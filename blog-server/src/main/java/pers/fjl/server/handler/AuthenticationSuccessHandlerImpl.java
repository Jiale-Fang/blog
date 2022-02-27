package pers.fjl.server.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.Message;
import pers.fjl.common.po.User;
import pers.fjl.common.utils.JWTUtils;
import pers.fjl.server.dao.UserDao;
import pers.fjl.server.service.ResourceService;
import pers.fjl.server.utils.BeanCopyUtils;
import pers.fjl.server.utils.RedisUtil;
import pers.fjl.server.utils.UserUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


/**
 * 登录成功处理
 */
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    @Resource
    private UserDao userDao;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ResourceService resourceService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        // 返回登录信息
        User user = BeanCopyUtils.copyObject(UserUtils.getLoginUser(), User.class);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        try {
            //认证成功，生成jwt令牌
            HashMap<String, String> payload = new HashMap<>();
            payload.put("id", String.valueOf(user.getUid()));
            payload.put("lastIp", user.getLastIp());
            payload.put("username", user.getUsername());
            String token = JWTUtils.getToken(payload);
            //获取用户权限并放入缓存
            List<String> userResource = resourceService.getUserResource(user.getUid());
            redisUtil.set(String.valueOf(user.getUid()), userResource.toArray(), 1800);
            httpServletResponse.getWriter().write(JSON.toJSONString(new Result(true, token, "token生成成功", user)));
        } catch (Exception e) {
            e.printStackTrace();
            httpServletResponse.getWriter().write(JSON.toJSONString(new Result(false, e.getMessage(),MessageConstant.ERROR)));
        }

        // 更新用户ip，最近登录时间
        updateUserInfo();
    }

    /**
     * 更新用户信息
     */
    @Async
    public void updateUserInfo() {
        User user = new User();
        user.setUid(UserUtils.getLoginUser().getUid());
        user.setLastIp(UserUtils.getLoginUser().getLastIp());
        user.setIpSource(UserUtils.getLoginUser().getIpSource());
        user.setDataStatus(true);
//                .lastLoginTime(UserUtils.getLoginUser().getLastLoginTime())

        userDao.updateById(user);
    }

}
