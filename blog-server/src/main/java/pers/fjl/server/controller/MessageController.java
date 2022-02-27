package pers.fjl.server.controller;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.Message;
import pers.fjl.server.annotation.IpRequired;
import pers.fjl.server.service.MessageService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 留言功能的前端控制器
 * </p>
 *
 * @author fangjiale
 * @since 2021-02-08
 */
@RestController
@CrossOrigin
@Api(value = "留言模块", description = "留言模块的接口信息")
@RequestMapping("/message")
public class MessageController {
    @Resource
    private MessageService messageService;

    @GetMapping("/getMessageList")
    public Result getMessageList() {
        return new Result(true, MessageConstant.OK, "获取留言列表信息成功", messageService.getMessageList());
    }

    @PostMapping("/getMessagePage")
    public Result getMessagePage(@RequestBody QueryPageBean queryPageBean) {
        return new Result(true, MessageConstant.OK, "获取留言列表信息成功", messageService.getMessagePage(queryPageBean));
    }

    @IpRequired
    @PostMapping("/add")
    public Result addMessage(@RequestBody Message message, HttpServletRequest request) {
        boolean flag = messageService.addMessage(message, (String) request.getAttribute("host"));
        if (flag) {
            return new Result(true, "添加留言成功", MessageConstant.OK);
        } else {
            return new Result(false, "添加留言失败", MessageConstant.ERROR);
        }

    }
}

