package pers.fjl.server.controller.admin;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.User;
import pers.fjl.server.annotation.LoginRequired;
import pers.fjl.server.service.ReportService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 后台数据统计模块
 *
 * @author fangjiale 2021年02月17日
 */

@Api(value = "后台数据统计模块", description = "后台数据统计模块的接口信息")
@RequestMapping("/report")
@RestController
@CrossOrigin
public class ReportController {
    @Resource
    private ReportService reportService;

    @LoginRequired
    @GetMapping("/getReport")
    public Result getReport(HttpServletRequest request) throws Exception {
        User user = (User) request.getAttribute("currentUser");
        return new Result(true, MessageConstant.OK, "获取博文数据成功!", reportService.getReport(user.getUid()));
    }

    @LoginRequired
    @GetMapping("/getReport2")
    public Result getReport2(HttpServletRequest request) throws Exception {
        User user = (User) request.getAttribute("currentUser");
        return new Result(true, MessageConstant.OK, "获取单篇博文分析数据成功!", reportService.getReport2(user.getUid()));
    }
}
