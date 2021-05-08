package pers.fjl.server.controller.chatroom;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.ChatLog;
import pers.fjl.server.service.ChatLogService;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
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

    @PostMapping(value = "/addMessage")
    public Result addMessage(@RequestBody ChatLog chatLog){
        chatLogService.addMessage(chatLog);
        return new Result(true,"添加消息成功", MessageConstant.OK);
    }

    @PostMapping(value = "/getMessage")
    public Result getMessage(@RequestBody ChatLog chatLog){
        return new Result(true, MessageConstant.OK,"获取聊天记录成功", chatLogService.getMessage(chatLog));
    }

}

