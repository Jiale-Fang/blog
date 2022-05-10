package pers.fjl.server.controller.chatroom;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.ChatLog;
import pers.fjl.common.po.GroupChat;
import pers.fjl.common.po.User;
import pers.fjl.server.annotation.LoginRequired;
import pers.fjl.server.service.ChatLogService;
import pers.fjl.server.service.GroupChatService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fangjiale
 * @since 2021-04-12
 */
@Api(value = "聊天室模块", description = "聊天室模块的群聊控制器接口信息")
@RestController
@CrossOrigin
@RequestMapping("/groupChat")
public class GroupChatController {
    @Resource
    private GroupChatService groupChatService;

    @GetMapping("/admin/getMessage/{currentPage}")
    @ApiOperation(value = "获取群聊消息")
    @LoginRequired
    public Result getMessage(HttpServletRequest request, @PathVariable("currentPage") Integer currentPage){
        User user = (User) request.getAttribute("currentUser");
        return Result.ok("获取群聊消息成功", groupChatService.getMessage(user.getUid(),currentPage));
    }

    @PostMapping("/admin/addMessage")
    @ApiOperation(value = "发送群聊消息")
    public Result addMessage(@RequestBody GroupChat groupChat){
        groupChatService.addMessage(groupChat);
        return Result.ok("添加群聊消息成功");
    }

}

