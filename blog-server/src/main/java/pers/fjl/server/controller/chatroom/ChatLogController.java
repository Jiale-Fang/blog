package pers.fjl.server.controller.chatroom;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import pers.fjl.common.entity.Result;
import pers.fjl.common.po.ChatLog;
import pers.fjl.common.vo.ChatLogVO;
import pers.fjl.server.service.ChatLogService;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author fangjiale
 * @since 2021-04-12
 */
@Api(value = "聊天室模块", description = "聊天室模块的消息控制器接口信息")
@RestController
@CrossOrigin
@RequestMapping("/chatLog")
public class ChatLogController {
    @Resource
    private ChatLogService chatLogService;

    @PostMapping(value = "/admin/addMessage")
    @ApiOperation(value = "私聊添加消息")
    public Result addMessage(@RequestBody ChatLog chatLog) {
        chatLogService.addMessage(chatLog);
        return Result.ok("添加消息成功");
    }

    @PostMapping(value = "/admin/getMessage")
    @ApiOperation(value = "用户获取私聊信息")
    public Result getMessage(@RequestBody ChatLogVO chatLogVO) {
        return Result.ok("获取聊天记录成功", chatLogService.getMessage(chatLogVO));
    }

}

