package pers.fjl.server.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.server.service.RoleService;

import javax.annotation.Resource;

/**
 * 资源模块控制器
 *
 * @author fangjiale 2022年01月14日
 */

@Api(value = "角色模块", description = "角色模块的接口信息")
@RequestMapping("/role")
@RestController
@CrossOrigin
public class RoleController {
    @Resource
    private RoleService roleService;

    @ApiOperation(value = "获取后台角色数据")
    @GetMapping("/listRoles")
    public Result listRole(QueryPageBean queryPageBean){
        return new Result(true, MessageConstant.OK, "获取后台角色数据", roleService.listRoles(queryPageBean));
    }

}
