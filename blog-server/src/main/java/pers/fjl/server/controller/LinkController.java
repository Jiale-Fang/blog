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

import static pers.fjl.common.constant.OptTypeConst.SAVE;
import static pers.fjl.common.constant.OptTypeConst.UPDATE;

/**
 * <p>
 *  友链控制器
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
    public Result getLinkList(){
        return new Result(true, MessageConstant.OK, "获取友链列表信息成功", linkService.getLinkList());
    }

    @ApiOperation(value = "添加友链", notes = "添加友链")
    @PostMapping("/addLink")
    public Result addLink(@RequestBody Link link){
        if (linkService.addLink(link))
        return new Result(true, "添加友链成功，请等待管理员审核",MessageConstant.OK);
        return new Result(false, "添加友链失败！",MessageConstant.ERROR);
    }

    @ApiOperation(value = "获取友链分页数据", notes = "获取友链分页数据")
    @GetMapping("/listLink")
    public Result listLink(QueryPageBean queryPageBean){
        return new Result(true, MessageConstant.OK, "获取分页数据成功", linkService.listLink(queryPageBean));
    }

    /**
     * 修改友链展示状态
     *
     * @param link 友链
     * @return Result
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改友链禁用状态")
    @PutMapping("/disable")
    public Result updateLinkDisable(@RequestBody Link link) {
        linkService.updateLinkDisable(link);
        return new Result(true,"获取分页数据成功", MessageConstant.OK);
    }

}

