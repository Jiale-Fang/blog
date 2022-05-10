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

@Api(tags = "首页展示模块",value = "首页展示模块", description = "首页展示模块的接口信息")
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

    @ApiOperation(value = "首页分页查询", notes = "返回分页数据")
    @PostMapping("/findHomePage")
    public Result findHomePage(@RequestBody QueryPageBean queryPageBean) {
        return Result.ok("获取分页数据成功", blogService.findHomePage(queryPageBean));
    }

    @GetMapping("/getTypeCount")
    @ApiOperation(value = "首页分类数据", notes = "返回分类数据")
    public Result getTypeCount() {
        return Result.ok( "获取分类信息成功", typeService.getTypeCount());
    }

    @GetMapping("/getTagCount")
    @ApiOperation(value = "首页标签数据", notes = "返回标签数据")
    public Result getTagCount() {
        return Result.ok( "获取标签信息成功", tagService.getTagCount());
    }

    @GetMapping("/latestList")
    @ApiOperation(value = "首页最新博文列表", notes = "返回最新博文列表")
    public Result getRecommendList() {
        return Result.ok( "获取最新推荐信息成功", blogService.getLatestList());
    }

}
