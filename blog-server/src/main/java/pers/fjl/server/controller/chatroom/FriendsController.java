package pers.fjl.server.controller.chatroom;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.Friends;
import pers.fjl.common.po.User;
import pers.fjl.server.annotation.LoginRequired;
import pers.fjl.server.service.FriendsService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author fangjiale
 * @since 2021-04-12
 */
@Api(value = "聊天室模块", description = "聊天室模块的好友关系控制器接口信息")
@RestController
@CrossOrigin
@RequestMapping("/friends")
public class FriendsController {
    @Resource
    private FriendsService friendsService;

    @GetMapping("/admin/getFriendsList")
    @ApiOperation(value = "用户获取好友列表")
    @LoginRequired
    public Result getFriendsList(HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        return Result.ok("返回好友列表成功", friendsService.getFriendsList(user.getUid()));
    }

    @PostMapping("/admin/addFriend")
    @ApiOperation(value = "用户添加好友")
    public Result addFriend(@RequestBody Friends friends) {
        friendsService.addFriend(friends);
        return Result.ok("添加好友成功");
    }

}

