package pers.fjl.server.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.server.service.OperationLogService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志控制器
 */
@Api(value = "日志模块")
@RequestMapping("/logs")
@RestController
@CrossOrigin
public class LogController {
    @Resource
    private OperationLogService operationLogService;

    /**
     * 查看操作日志
     *
     * @param queryPageBean 条件
     * @return {@link Result} 日志列表
     */
    @ApiOperation(value = "查看操作日志")
    @GetMapping("/admin/operation")
    public Result listOperationLogs(QueryPageBean queryPageBean) {
        return Result.ok("获取日志列表成功", operationLogService.listOperationLogs(queryPageBean));
    }

    /**
     * 删除操作日志
     *
     * @param logIdList 日志id列表
     * @return {@link Result}
     */
    @ApiOperation(value = "删除操作日志")
    @DeleteMapping("/admin/delete")
    public Result deleteOperationLogs(@RequestBody List<Integer> logIdList) {
        operationLogService.removeByIds(logIdList);
        return Result.ok("删除成功");
    }

}
