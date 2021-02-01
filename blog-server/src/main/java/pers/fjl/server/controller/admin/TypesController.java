package pers.fjl.server.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.Type;
import pers.fjl.server.annotation.LoginRequired;
import pers.fjl.server.service.TypeService;

import javax.annotation.Resource;

/**
 * 分类管理模块
 *
 * @author fangjiale 2021年01月27日
 */

@Api(value = "后台分类管理模块", description = "分类管理模块的接口信息")
@RequestMapping("/types2")
@RestController
@CrossOrigin
public class TypesController {
    @Resource
    private TypeService typeService;

    @LoginRequired
    @ApiOperation(value = "分页查询", notes = "返回分页数据")
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        return new Result(true, MessageConstant.OK, "获取分页数据成功", typeService.findPage(queryPageBean));
    }

    @PostMapping("/add")
    public Result addType(@RequestBody Type type) {
        boolean flag = typeService.addType(type);
        return new Result(flag, "添加成功", MessageConstant.OK);
    }

    @GetMapping("/getTypeList")
    public Result getTypeList() {
        return new Result(true, MessageConstant.OK, "获取分类信息成功", typeService.getTypeList());
    }

}
