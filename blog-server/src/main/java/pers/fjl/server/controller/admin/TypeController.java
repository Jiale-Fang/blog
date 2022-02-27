package pers.fjl.server.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.Type;
import pers.fjl.server.annotation.LoginRequired;
import pers.fjl.server.annotation.OptLog;
import pers.fjl.server.service.TypeService;

import javax.annotation.Resource;

import static pers.fjl.common.constant.OptTypeConst.SAVE;
import static pers.fjl.common.constant.OptTypeConst.UPDATE;

/**
 * 分类管理模块
 *
 * @author fangjiale 2021年01月27日
 */

@Api(value = "后台分类管理模块", description = "分类管理模块的接口信息")
@RequestMapping("/type")
@RestController
@CrossOrigin
public class TypeController {
    @Resource
    private TypeService typeService;

    @LoginRequired
    @ApiOperation(value = "后台分类分页查询", notes = "返回分页数据")
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        return new Result(true, MessageConstant.OK, "获取分页数据成功", typeService.findPage(queryPageBean));
    }

    @ApiOperation(value = "管理员后台分类数据", notes = "返回分页数据")
    @PostMapping("/adminType")
    public Result adminType(@RequestBody QueryPageBean queryPageBean) {
        return new Result(true, MessageConstant.OK, "获取后台分类数据成功", typeService.adminType(queryPageBean));
    }

    @OptLog(optType = SAVE)
    @PostMapping("/add")
    @ApiOperation(value = "后台分类添加", notes = "添加")
    public Result addType(@RequestBody Type type) {
        boolean flag = typeService.addType(type);
        if (flag)
        return new Result(true, "添加成功", MessageConstant.OK);
        return new Result(false, "添加失败，要添加的分类已存在", MessageConstant.ERROR);
    }

    @GetMapping("/getTypeList")
    public Result getTypeList() {
        return new Result(true, MessageConstant.OK, "获取分类信息成功", typeService.getTypeList());
    }


}
