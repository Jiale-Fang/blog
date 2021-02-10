package pers.fjl.extension.controller;


import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.Result;
import pers.fjl.extension.po.Message;
import pers.fjl.extension.service.MessageService;

import javax.annotation.Resource;

/**
 * <p>
 *  留言功能的前端控制器
 * </p>
 *
 * @author fangjiale
 * @since 2021-02-08
 */
@RestController
@CrossOrigin
@RequestMapping("/message")
public class MessageController {
    @Resource
    private MessageService messageService;

    @GetMapping("/getMessageList")
    public Result getMessageList(){
        return new Result(true, MessageConstant.OK,"获取留言列表信息成功",messageService.getMessageList());
    }

    @PostMapping("/add")
    public Result addMessage(@RequestBody Message message){
        return new Result(true, MessageConstant.OK,"添加留言成功",messageService.addMessage(message));
    }
}

