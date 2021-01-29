package pers.fjl.server.controller.admapi;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.User;
import pers.fjl.server.annotation.IpRequired;
import pers.fjl.server.service.UserService;
import pers.fjl.server.utils.JWTUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

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

    @PostMapping("/login")
    @IpRequired
    public Result login(@RequestBody User user, HttpServletRequest request) {
        log.info("用户名:[{}]", user.getUsername());
        log.info("密码:[{}]", user.getPassword());
        try {
            //认证成功，生成jwt令牌
            user.setLastIp((String) request.getAttribute("host"));
            System.out.println("indexAdminController:" + (String) request.getAttribute("host"));
            User userDB = userService.login(user);
            HashMap<String, String> payload = new HashMap<>();
            payload.put("id", String.valueOf(userDB.getUid()));
            payload.put("lastIp", userDB.getLastIp());
            payload.put("username", userDB.getUsername());
            String token = JWTUtils.getToken(payload);

            return new Result(true, token, "token生成成功", userDB);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.ERROR, "token生成失败,请检查你的账号与密码是否匹配");
    }
}
