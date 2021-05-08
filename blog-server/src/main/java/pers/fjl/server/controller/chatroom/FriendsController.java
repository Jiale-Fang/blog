package pers.fjl.server.controller.chatroom;


import io.swagger.annotations.Api;
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

    @GetMapping("/getFriendsList")
    @LoginRequired
    public Result getFriendsList(HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        return new Result(true, MessageConstant.OK, "返回好友列表成功", friendsService.getFriendsList(user.getUid()));
    }

    @PostMapping("/addFriend")
    public Result addFriend(@RequestBody Friends friends) {
        friendsService.addFriend(friends);
        return new Result(true, "添加好友成功", MessageConstant.OK);
    }

}

