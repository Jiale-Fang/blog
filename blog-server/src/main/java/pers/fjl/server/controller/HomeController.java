package pers.fjl.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.User;
import pers.fjl.server.annotation.LoginRequired;
import pers.fjl.server.service.BlogService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 首页展示模块
 *
 * @author fangjiale 2021年01月29日
 */

@Api(value = "首页展示模块", description = "首页展示模块的接口信息")
@RequestMapping("/home")
@RestController
@CrossOrigin
public class HomeController {
    @Resource
    private BlogService blogService;

    @LoginRequired
    @ApiOperation(value = "分页查询", notes = "返回分页数据")
    @PostMapping("/findHomePage")
    public Result findHomePage(@RequestBody QueryPageBean queryPageBean, HttpServletRequest request) {
        return new Result(true, MessageConstant.OK, "获取分页数据成功", blogService.findHomePage(queryPageBean));
    }
}
