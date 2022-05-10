package pers.fjl.server.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.Tag;
import pers.fjl.server.annotation.LoginRequired;
import pers.fjl.server.annotation.OptLog;
import pers.fjl.server.service.TagService;

import javax.annotation.Resource;

import java.util.List;

import static pers.fjl.common.constant.OptTypeConst.*;

/**
 * 标签管理模块
 *
 * @author fangjiale 2021年01月27日
 */

@Api(value = "后台标签管理模块", description = "标签管理模块的接口信息")
@RequestMapping("/tag")
@RestController
@CrossOrigin
public class TagController {
    @Resource
    private TagService tagService;

    @LoginRequired
    @ApiOperation(value = "个人后台标签分页查询", notes = "返回分页数据")
    @PostMapping("/admin/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        return Result.ok("获取分页数据成功", tagService.findPage(queryPageBean));
    }

    @ApiOperation(value = "管理员后台标签数据", notes = "返回分页数据")
    @PostMapping("/admin/tagList")
    public Result adminTag(@RequestBody QueryPageBean queryPageBean) {
        return Result.ok("获取后台标签数据成功", tagService.adminTag(queryPageBean));
    }

//    @OptLog(optType = SAVE)
//    @PostMapping("/add")
//    public Result addType(@RequestBody Type type){
//        boolean flag = typeService.addType(type);
//        return new Result(flag,"添加成功", MessageConstant.OK);
//    }

    @ApiOperation(value = "获取标签列表", notes = "返回分页数据")
    @GetMapping("/getTagList")
    public Result getTagList(){
        return Result.ok("获取成功", tagService.getTagList());
    }

    @ApiOperation(value = "搜索标签", notes = "返回分页数据")
    @GetMapping("/admin/search")
    public Result searchTags(QueryPageBean queryPageBean){
        return Result.ok("搜索成功", tagService. searchTags(queryPageBean));
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @PostMapping("/admin/saveOrUpdate")
    @ApiOperation(value = "后台标签添加或更改", notes = "添加或更改")
    public Result saveOrUpdateTag(@RequestBody Tag tag) {
        boolean flag = tagService.saveOrUpdateType(tag);
        if (flag)
            return Result.ok("添加或更改标签成功");
        return Result.fail("添加失败，要添加或更改的标签已存在");
    }

    @OptLog(optType = REMOVE)
    @DeleteMapping("/admin/delete")
    @ApiOperation(value = "删除后台标签", notes = "删除标签")
    public Result delete(@RequestBody List<Integer> tagIdList) {
        tagService.delete(tagIdList);
        return Result.ok("删除标签成功", MessageConstant.OK);
    }


}
