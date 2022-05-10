package pers.fjl.server.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.User;
import pers.fjl.server.service.ArchivesService;
import pers.fjl.server.service.BlogService;
import pers.fjl.server.service.UserService;

import javax.annotation.Resource;

/**
 * 归档模块
 *
 * @author fangjiale 2021年01月26日
 */

@Api(value = "归档模块", description = "归档模块的接口信息")
@RequestMapping("/archives")
@RestController
@CrossOrigin
public class ArchivesController {

    @Resource
    private ArchivesService archivesService;

    @GetMapping("/getArchivesList")
    @ApiOperation(value = "获取归档列表")
    public Result getArchivesList() {
        return Result.ok("获取归档信息成功", archivesService.getArchivesList());
    }
}

