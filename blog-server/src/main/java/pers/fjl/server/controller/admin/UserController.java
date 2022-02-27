package pers.fjl.server.controller.admin;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.dto.UserOnlineDTO;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.User;
import pers.fjl.server.service.UserService;

import javax.annotation.Resource;

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
    @ApiOperation(value = "用户注册", notes = "用户注册")
    public Result login(@RequestBody User user) {
        if (userService.add(user))
        return new Result(true, "注册成功", MessageConstant.OK);
        return new Result(false, "用户名已被注册", MessageConstant.ERROR);
    }

    @ApiOperation(value = "用户更新个人信息", notes = "用户更新个人信息")
    @PutMapping("/updateUser")
    public Result updateUser(@RequestBody User user) {
        if (userService.updateUser(user))
        return new Result(true, "更新用户信息成功", MessageConstant.OK);
        return new Result(false, "用户名已被注册", MessageConstant.ERROR);
    }

    @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
    @GetMapping("/getUserList")
    public Result getUserList(){
        return new Result(true, MessageConstant.OK, "获取用户信息成功", userService.getUserList());
    }

    @ApiOperation(value = "获取用户分布地区", notes = "获取用户分布地区")
    @GetMapping("/listUserAreas")
    public Result listUserAreas(){
        return new Result(true, MessageConstant.OK, "获取用户分布信息成功", userService.listUserAreas());
    }

    @ApiOperation(value = "获取后台用户列表", notes = "获取后台用户列表")
    @GetMapping("/adminUser")
    public Result adminUser(QueryPageBean queryPageBean){
        return new Result(true, MessageConstant.OK, "获取后台用户列表成功", userService.adminUser(queryPageBean));
    }

    /**
     * 查看在线用户
     *
     * @param queryPageBean 条件
     * @return 在线用户列表
     */
    @ApiOperation(value = "查看在线用户")
    @GetMapping("/online")
    public Result listOnlineUsers(QueryPageBean queryPageBean) {
        return new Result(true, MessageConstant.OK, "获取在线用户列表成功", userService.listOnlineUsers(queryPageBean));
    }
}

