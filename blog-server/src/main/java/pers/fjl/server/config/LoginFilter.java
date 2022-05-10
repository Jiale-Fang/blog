package pers.fjl.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pers.fjl.server.dto.UserDetailDTO;
import pers.fjl.server.exception.BizException;
import pers.fjl.server.service.UserService;
import pers.fjl.server.utils.IpUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * 自定义前后端分离认证Security的Filter（否则login默认只能接受表单请求，无法接受json）
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    @Resource
    private SessionRegistry sessionRegistry;
    @Resource
    private UserService userService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //1.判断是否是POST请求
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        //2.判断是否是json格式请求类型
        if (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
            //3.从json数据中获取用户输入用户名和密码进行验证
            try {
                Map<String, String> userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                String username = userInfo.get(getUsernameParameter());
                String password = userInfo.get(getPasswordParameter());
                String code = userInfo.get("code");
                String verKey = userInfo.get("verKey");
                // 创建登录信息
                UserDetailDTO userDetailDTO = null;
                // 获取用户ip信息
                String ipAddress = IpUtils.getIpAddr(request);
                String ipSource = IpUtils.getIpSource(ipAddress);
                if (verKey.length() != 0) {
                    userService.verifyCode(verKey, code);
                }
                if (Objects.nonNull(username)) {    // 获取用户名
                    // 返回数据库用户信息
                    userDetailDTO = userService.getUserDetail(username, request, ipAddress, ipSource);
                }
                // 判断账号是否禁用
                if (!userDetailDTO.isStatus()) {
                    throw new AuthenticationServiceException("账号已被禁用");
                }
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
                // 将登录信息放入springSecurity管理
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetailDTO, null, userDetailDTO.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                //  用户名密码验证通过后，将含有用户信息的token注册session
                sessionRegistry.registerNewSession(request.getSession().getId(), auth.getPrincipal());
                this.setDetails(request, auth);
                try {
                    return this.getAuthenticationManager().authenticate(authRequest);
                } catch (BadCredentialsException e) {
                    throw new AuthenticationServiceException("请输入正确的用户名或密码");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return super.attemptAuthentication(request, response);
    }
}

