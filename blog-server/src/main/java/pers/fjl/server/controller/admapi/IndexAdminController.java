package pers.fjl.server.controller.admapi;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.wf.captcha.SpecCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.CacheKey;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.User;
import pers.fjl.common.utils.JWTUtils;
import pers.fjl.common.vo.UserVo;
import pers.fjl.server.annotation.IpRequired;
import pers.fjl.server.service.ResourceService;
import pers.fjl.server.service.UserService;
import pers.fjl.server.utils.RedisUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static pers.fjl.common.utils.JWTUtils.getTokenInfo;

/**
 * <p>
 * 信息管理控制器
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-27
 */
@CrossOrigin
@RestController
@Slf4j
@Api(value = "信息管理模块", description = "管理用户信息验证")
@RequestMapping("/admapi")
public class IndexAdminController {
    @Resource
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ResourceService resourceService;

    @ApiOperation(value = "普通用户登录接口")
    @PostMapping("/login")
    @IpRequired
    public Result login(@RequestBody UserVo userVo, HttpServletRequest request) {
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        userService.verifyCode(userVo.getVerKey(), userVo.getCode(), (String) redisUtil.get(userVo.getVerKey())); // 验证如果不通过，后台直接抛异常
        log.info("用户名:[{}]", user.getUsername());
        request.getSession().setAttribute("username", user.getUsername());   // 给websocket取出
        log.info("密码:[{}]", user.getPassword());
        try {
            //认证成功，生成jwt令牌
            user.setLastIp((String) request.getAttribute("host"));
            User userDB = userService.login(user);
            HashMap<String, String> payload = new HashMap<>();
            payload.put("id", String.valueOf(userDB.getUid()));
            payload.put("lastIp", userDB.getLastIp());
            payload.put("username", userDB.getUsername());
            String token = JWTUtils.getToken(payload);

            //获取用户权限并放入缓存
            List<String> userResource = resourceService.getUserResource(userDB.getUid());
            redisUtil.set(String.valueOf(userDB.getUid()), userResource.toArray(), 1800);

            return new Result(true, token, "token生成成功", userDB);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "token生成失败,请检查你的账号与密码是否匹配", MessageConstant.ERROR);
    }

    @ApiOperation(value = "验证码")
    @RequestMapping("/captcha")
    public Result captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String verCode = specCaptcha.text().toLowerCase();
        String key = UUID.randomUUID().toString();
        // 存入redis并设置过期时间为10分钟
        redisUtil.set(key, verCode, 600);
        request.getSession().setAttribute("CAPTCHA", verCode);  //存入session
        // 将key和base64返回给前端
        return new Result(true, key, MessageConstant.VERIFICATION_CODE_SUCCESS, specCaptcha.toBase64());
    }

    @ApiOperation(value = "注销登录接口")
    @PostMapping("/logout")
    public Result logout() {
        return new Result(true, MessageConstant.OK, "登出成功");
    }
}
