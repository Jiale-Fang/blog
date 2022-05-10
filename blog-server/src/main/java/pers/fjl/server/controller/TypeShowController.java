package pers.fjl.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.server.annotation.LoginRequired;
import pers.fjl.server.service.BlogService;
import pers.fjl.server.service.TypeService;

import javax.annotation.Resource;

/**
 * 分类展示模块
 *
 * @author fangjiale 2021年01月31日
 */

@Api(value = "分类展示模块", description = "分类展示模块的接口信息")
@RequestMapping("/typeShow")
@RestController
@CrossOrigin
public class TypeShowController {
    @Resource
    private BlogService blogService;

    @ApiOperation(value = "根据分类分页展示", notes = "返回分页数据")
    @PostMapping("/getById")
    public Result getByTypeId(@RequestBody QueryPageBean queryPageBean) {
        if (queryPageBean.getTypeId() == null)
            return Result.ok("获取分类信息成功", blogService.findHomePage(queryPageBean));
        return Result.ok("根据id获取分类信息成功", blogService.getByTypeId(queryPageBean));
    }
}
