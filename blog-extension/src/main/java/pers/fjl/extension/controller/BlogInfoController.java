package pers.fjl.extension.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.extension.po.BlogInfo;
import pers.fjl.extension.service.BlogInfoService;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(value = "/search")
public class BlogInfoController {

    @Resource
    private BlogInfoService blogInfoService;

    @ApiOperation(value = "分页条件查询", notes = "返回分页数据")
    @PostMapping("/searchPage2")
    public Result searchPage(@RequestBody QueryPageBean queryPageBean) throws IOException {
        return new Result(true, MessageConstant.OK, "获取分页数据成功", blogInfoService.searchPage(queryPageBean));
    }

    @ApiOperation(value = "分页高亮条件查询", notes = "返回分页数据")
    @PostMapping("/searchPage1")
    public Result highLightSearchPage(@RequestBody QueryPageBean queryPageBean) throws IOException {
        return new Result(true, MessageConstant.OK, "获取分页数据成功", blogInfoService.highLightSearchPage(queryPageBean));
    }

    @ApiOperation(value = "分页查询", notes = "返回分页数据")
    @PostMapping("/homePage")
    public Result homePage(@RequestBody QueryPageBean queryPageBean) throws IOException {
        return new Result(true, MessageConstant.OK, "获取分页数据成功", blogInfoService.homePage(queryPageBean));
    }

    @GetMapping(value = "/importData")
    public Result importData() {
        blogInfoService.importData();
        return new Result(true, "引入数据成功！", MessageConstant.OK);
    }

    @PostMapping(value = "/save")
    public Result save(@RequestBody BlogInfo blogInfo){
        blogInfoService.save(blogInfo);
        return new Result(true, "保存数据成功！", MessageConstant.OK);
    }

    @GetMapping(value = "/test1")
    public Result test1(){
        return new Result(true, "成功！", MessageConstant.OK);
    }
}
