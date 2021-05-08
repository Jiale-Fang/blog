package pers.fjl.server.controller.chatroom;


import io.swagger.annotations.Api;
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

    @GetMapping("/getMessage")
    @LoginRequired
    public Result getMessage(HttpServletRequest request){
        User user = (User) request.getAttribute("currentUser");
        return new Result(true, MessageConstant.OK, "获取群聊消息成功", groupChatService.getMessage(user.getUid()));
    }

    @PostMapping("/addMessage")
    public Result addMessage(@RequestBody GroupChat groupChat){
        groupChatService.addMessage(groupChat);
        return new Result(true, "添加群聊消息成功",MessageConstant.OK );
    }

}

