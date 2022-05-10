package pers.fjl.server.interceptors;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import pers.fjl.common.po.User;
import pers.fjl.common.utils.JWTUtils;
import pers.fjl.server.annotation.IpRequired;
import pers.fjl.server.annotation.LoginRequired;
import pers.fjl.server.exception.BizException;
import pers.fjl.server.service.UserService;
import pers.fjl.server.utils.IpUtils;
import pers.fjl.server.utils.RedisUtil;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

import static pers.fjl.common.enums.StatusCodeEnum.TOKEN_EXPIRED;
import static pers.fjl.common.constant.RedisConst.TOKEN_ALLOW_LIST;
import static pers.fjl.common.utils.JWTUtils.getTokenInfo;

@Slf4j
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Resource
    private UserService userServiceAuto;
    @Resource
    private RedisUtil redisUtilAuto;

    private static UserService userService;
    private static RedisUtil redisUtil;

    @PostConstruct
    public void init() {
        userService = this.userServiceAuto;
        redisUtil = this.redisUtilAuto;//将注入的对象交给静态对象管理
    }

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        log.info("request:[{}]", request.toString());
        log.info("request.getRemoteHost():[{}]", request.getRemoteHost());
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        IpRequired ipRequired = method.getAnnotation(IpRequired.class);
        if (ipRequired != null) {
            String ipAddress = IpUtils.getIpAddr(request);
            log.info("ipAddress:[{}]", ipAddress);
            request.setAttribute("host", ipAddress);
            return true;
        }

        // 判断接口是否需要登录
        LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
        // 有 @LoginRequired 注解，需要认证
        if (loginRequired != null) {
            // 执行认证
//            String token = request.getHeader("token");  // 从 http 请求头中取出 token
            String token = request.getHeader("Authorization");
            if (token == null) {
                throw new BizException("无token，请重新登录");
            }
            String userId;
            try {
                DecodedJWT verify = getTokenInfo(token);
                userId = verify.getClaim("id").asString();
            } catch (JWTDecodeException e) {
                throw new BizException("token无效，请重新登录");
            }
            if (Objects.isNull(redisUtil.get(TOKEN_ALLOW_LIST + userId)))  {    // token已经失效
                throw new BizException(TOKEN_EXPIRED);
            }
            User user = userService.findById(Long.parseLong(userId));
            if (user == null) {
                throw new BizException("用户不存在，请重新登录");
            }
            // 验证 token
            try {
                JWTUtils.verify(token);
            } catch (JWTVerificationException e) {
                throw new BizException("token无效，请重新登录");
            }
            request.setAttribute("currentUser", user);
            return true;
        }
        return true;
    }

}
