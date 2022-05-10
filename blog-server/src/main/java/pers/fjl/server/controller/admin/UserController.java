package pers.fjl.server.controller.admin;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.User;
import pers.fjl.common.vo.ResetPasswordVO;
import pers.fjl.common.vo.UpdateUserVO;
import pers.fjl.common.vo.UserDisableVO;
import pers.fjl.server.annotation.OptLog;
import pers.fjl.server.face.view.FaceRegisterVO;
import pers.fjl.server.service.UserService;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.io.IOException;
import java.util.List;

import static pers.fjl.common.constant.OptTypeConst.REMOVE;
import static pers.fjl.common.constant.OptTypeConst.UPDATE;

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
    public Result register(@RequestBody User user) {
        if (userService.add(user))
            return Result.ok("注册成功");
        return Result.fail("用户名已被注册");
    }

    @PostMapping("/registerByFace")
    @ApiOperation(value = "用户人脸识别注册", notes = "用户人脸识别")
    public Result registerByFace(@RequestBody FaceRegisterVO faceRegisterVO) throws IOException {
        if (userService.registerByFace(faceRegisterVO))
            return Result.ok("注册成功，快来刷脸登录吧！");
        return Result.fail("该张人像已被注册");
    }

    @OptLog(optType = REMOVE)
    @DeleteMapping("/admin/delete")
    @ApiOperation(value = "删除用户", notes = "删除用户")
    public Result delete(@RequestBody List<Long> uidList) {
        userService.delete(uidList);
        return Result.ok("该用户以及与该用户发布的博客、评论等有关的信息都已删除");
    }

    @ApiOperation(value = "用户更新个人信息", notes = "用户更新个人信息")
    @PutMapping("/admin/updateUser")
    public Result updateUser(@RequestBody UpdateUserVO updateUserVO) {
        if (userService.updateUser(updateUserVO))
            return Result.ok("更新用户信息成功");
        return Result.fail("用户名已被注册");
    }

    @ApiOperation(value = "用户找回密码", notes = "用户找回密码")
    @PutMapping("/resetPassword")
    public Result resetPassword(@RequestBody ResetPasswordVO resetPasswordVO) {
        userService.resetPassword(resetPasswordVO);
        return Result.ok("修改密码成功");
    }

    @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
    @GetMapping("/getUserList")
    public Result getUserList() {
        return Result.ok("获取用户信息成功", userService.getUserList());
    }

    @ApiOperation(value = "获取用户分布地区", notes = "获取用户分布地区")
    @GetMapping("/admin/listUserAreas")
    public Result listUserAreas() {
        return Result.ok("获取用户分布信息成功", userService.listUserAreas());
    }

    @ApiOperation(value = "获取后台用户列表", notes = "获取后台用户列表")
    @GetMapping("/admin/userList")
    public Result adminUser(QueryPageBean queryPageBean) {
        return Result.ok("获取后台用户列表成功", userService.adminUser(queryPageBean));
    }

    /**
     * 查看在线用户
     *
     * @param queryPageBean 条件
     * @return 在线用户列表
     */
    @ApiOperation(value = "查看在线用户")
    @GetMapping("/admin/online")
    public Result listOnlineUsers(QueryPageBean queryPageBean) {
        return Result.ok("获取在线用户列表成功", userService.listOnlineUsers(queryPageBean));
    }

    /**
     * 修改用户禁用状态
     *
     * @param userDisableVO 用户禁用信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改用户禁用状态")
    @PutMapping("/admin/disable")
    public Result updateUserDisable(@Valid @RequestBody UserDisableVO userDisableVO) {
        userService.updateUserDisable(userDisableVO);
        return Result.ok("修改禁用状态成功");
    }

    @ApiOperation(value = "发送邮箱验证码")
    @GetMapping("/code")
    public Result sendCode(String email) {
        userService.sendCode(email);
        return Result.ok("获取验证码成功（30分钟内有效）");
    }

    @ApiOperation(value = "强制用户下线")
    @DeleteMapping("/admin/online/{uid}")
    public Result removeOnlineUser(@PathVariable("uid") Long uid){
        userService.removeOnlineUser(uid);
        return Result.ok("下线用户成功");
    }


}

