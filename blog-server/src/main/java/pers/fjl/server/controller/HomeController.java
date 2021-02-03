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
import pers.fjl.server.service.TagService;
import pers.fjl.server.service.TypeService;

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
    @Resource
    private TagService tagService;
    @Resource
    private TypeService typeService;

    @ApiOperation(value = "分页查询", notes = "返回分页数据")
    @PostMapping("/findHomePage")
    public Result findHomePage(@RequestBody QueryPageBean queryPageBean) {
        return new Result(true, MessageConstant.OK, "获取分页数据成功", blogService.findHomePage(queryPageBean));
    }

    @GetMapping("/getTypeCount")
    public Result getTypeCount() {
        return new Result(true, MessageConstant.OK, "获取分类信息成功", typeService.getTypeCount());
    }

    @GetMapping("/getTagCount")
    public Result getTagCount() {
        return new Result(true, MessageConstant.OK, "获取标签信息成功", tagService.getTagCount());
    }

    @GetMapping("/latestList")
    public Result getRecommendList() {
        return new Result(true, MessageConstant.OK, "获取最新推荐信息成功", blogService.getLatestList());
    }

}
