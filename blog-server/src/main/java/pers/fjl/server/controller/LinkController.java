package pers.fjl.server.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.Link;
import pers.fjl.server.annotation.OptLog;
import pers.fjl.server.service.LinkService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static pers.fjl.common.constant.OptTypeConst.*;

/**
 * <p>
 * 友链控制器
 * </p>
 *
 * @author fangjiale
 * @since 2021-02-08
 */
@RestController
@CrossOrigin
@Api(value = "友链模块", description = "友链模块的接口信息")
@RequestMapping("/link")
public class LinkController {
    @Resource
    private LinkService linkService;

    @ApiOperation(value = "获取友链列表", notes = "获取友链列表")
    @GetMapping("/getLink")
    public Result getLinkList() {
        return Result.ok("获取友链列表信息成功", linkService.getLinkList());
    }

    @ApiOperation(value = "添加友链", notes = "添加友链")
    @PostMapping("/addLink")
    public Result addLink(@RequestBody Link link) {
        if (linkService.addLink(link))
            return Result.ok("添加友链成功，请等待管理员审核");
        return Result.fail("添加友链失败！");
    }

    /**
     * 保存或修改友链
     *
     * @param link 友链信息
     * @return {@link Result}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或修改友链")
    @PostMapping("/admin/saveOrUpdateFriendLink")
    public Result saveOrUpdateFriendLink(@RequestBody Link link) {
        linkService.saveOrUpdateFriendLink(link);
        return Result.ok("添加或编辑友链成功");
    }

    /**
     * 删除友链
     *
     * @param linkIdList 友链id列表
     * @return {@link Result}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除友链")
    @DeleteMapping("/admin/delete")
    public Result deleteFriendLink(@RequestBody List<Long> linkIdList) {
        linkService.removeByIds(linkIdList);
        return Result.ok("删除友链成功");
    }

    @ApiOperation(value = "获取友链分页数据", notes = "获取友链分页数据")
    @GetMapping("/listLink")
    public Result listLink(QueryPageBean queryPageBean) {
        return Result.ok("获取分页数据成功", linkService.listLink(queryPageBean));
    }

    /**
     * 修改友链展示状态
     *
     * @param link 友链
     * @return Result
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改友链禁用状态")
    @PutMapping("/admin/disable")
    public Result updateLinkDisable(@RequestBody Link link) {
        linkService.updateLinkDisable(link);
        return Result.ok("获取分页数据成功");
    }

}

