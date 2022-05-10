package pers.fjl.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.server.search.index.BlogInfo;
import pers.fjl.server.service.BlogInfoService;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@CrossOrigin
@Api(tags = "博客主页控制器")
@RequestMapping(value = "/search")
public class BlogInfoController {

    @Resource
    private BlogInfoService blogInfoService;

    @ApiOperation(value = "分页条件查询", notes = "返回分页数据")
    @PostMapping("/searchPage2")
    public Result searchPage(@RequestBody QueryPageBean queryPageBean) throws IOException {
        return Result.ok("获取分页数据成功", blogInfoService.searchPage(queryPageBean));
    }

    @ApiOperation(value = "分页高亮条件查询", notes = "返回分页数据")
    @PostMapping("/searchPage1")
    public Result highLightSearchPage(@RequestBody QueryPageBean queryPageBean) throws IOException {
        return Result.ok("获取分页数据成功", blogInfoService.highLightSearchPage(queryPageBean));
    }

    @ApiOperation(value = "分页查询", notes = "返回分页数据")
    @PostMapping("/homePage")
    public Result homePage(@RequestBody QueryPageBean queryPageBean) throws IOException {
        return Result.ok("获取分页数据成功", blogInfoService.homePage(queryPageBean));
    }

    @GetMapping(value = "/importData")
    public Result importData() {
        blogInfoService.importData();
        return Result.ok("引入数据成功！");
    }

    @PostMapping(value = "/save")
    public Result save(@RequestBody BlogInfo blogInfo){
        blogInfoService.save(blogInfo);
        return Result.ok("保存数据成功！");
    }

}
