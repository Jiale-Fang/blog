package pers.fjl.extension.controller;


import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.Result;
import pers.fjl.extension.po.Link;
import pers.fjl.extension.service.LinkService;

import javax.annotation.Resource;

/**
 * <p>
 *  友链控制器
 * </p>
 *
 * @author fangjiale
 * @since 2021-02-08
 */
@RestController
@CrossOrigin
@RequestMapping("/link")
public class LinkController {
    @Resource
    private LinkService linkService;

    @GetMapping("/getLink")
    public Result getLinkList(){
        return new Result(true, MessageConstant.OK, "获取友链列表信息成功", linkService.getLinkList());
    }

    @PostMapping("/addLink")
    public Result addLink(@RequestBody Link link){
        return new Result(true, MessageConstant.OK, "添加友链成功，请等待管理员审核", linkService.addLink(link));
    }
}

