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

import java.util.List;

import static pers.fjl.common.constant.OptTypeConst.*;

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
    @PostMapping("/admin/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        return Result.ok("获取分页数据成功", typeService.findPage(queryPageBean));
    }

    @ApiOperation(value = "管理员后台分类数据", notes = "返回分页数据")
    @PostMapping("/admin/typeList")
    public Result adminType(@RequestBody QueryPageBean queryPageBean) {
        return Result.ok("获取后台分类数据成功", typeService.adminType(queryPageBean));
    }

    @OptLog(optType = SAVE)
    @PostMapping("/admin/add")
    @ApiOperation(value = "后台分类添加", notes = "添加")
    public Result addType(@RequestBody Type type) {
        boolean flag = typeService.addType(type);
        if (flag)
            return Result.ok("添加成功");
        return Result.fail("添加失败，要添加的分类已存在");
    }

    @GetMapping("/getTypeList")
    public Result getTypeList() {
        return Result.ok("获取分类信息成功", typeService.getTypeList());
    }

    @ApiOperation(value = "分类搜索", notes = "返回分页数据")
    @GetMapping("/admin/search")
    public Result searchTypes(QueryPageBean queryPageBean){
        return Result.ok("搜索成功", typeService. searchTypes(queryPageBean));
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @PostMapping("/admin/saveOrUpdate")
    @ApiOperation(value = "后台分类添加或更改", notes = "添加或更改")
    public Result saveOrUpdateType(@RequestBody Type type) {
        boolean flag = typeService.saveOrUpdateType(type);
        if (flag)
            return Result.ok("添加或更改分类成功");
        return Result.fail("添加失败，要添加的分类已存在");
    }

    @OptLog(optType = REMOVE)
    @DeleteMapping("/admin/delete")
    @ApiOperation(value = "删除后台分类", notes = "删除")
    public Result delete(@RequestBody List<Integer> typeIdList) {
        typeService.delete(typeIdList);
        return Result.ok("删除分类成功");
    }

}
