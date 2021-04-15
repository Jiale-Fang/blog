package pers.fjl.server.controller.admin;


import io.swagger.annotations.Api;
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
 * 用户模块
 *
 * @author fangjiale 2021年01月26日
 */

@Api(value = "用户模块", description = "用户模块的接口信息")
@RequestMapping("/user")
@RestController
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/add")
    public Result login(@RequestBody User user) {
        userService.add(user);
        return new Result(true, "注册成功", MessageConstant.OK);
    }

    @PutMapping("/updateUser")
    public Result updateUser(@RequestBody User user) {
        return new Result(true, MessageConstant.OK, "更新用户信息成功", userService.updateUser(user));
    }

    @GetMapping("/getUserList")
    public Result getUserList(){
        return new Result(true, MessageConstant.OK, "获取用户信息成功", userService.getUserList());
    }

}

