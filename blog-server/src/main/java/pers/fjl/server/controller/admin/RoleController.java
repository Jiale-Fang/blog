package pers.fjl.server.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.common.vo.RoleVO;
import pers.fjl.common.vo.UserRoleVO;
import pers.fjl.server.annotation.OptLog;
import pers.fjl.server.service.RoleService;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;

import static pers.fjl.common.constant.OptTypeConst.REMOVE;
import static pers.fjl.common.constant.OptTypeConst.SAVE_OR_UPDATE;

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
    @GetMapping("/admin/listRoles")
    public Result listRole(QueryPageBean queryPageBean) {
        return Result.ok("获取后台角色数据", roleService.listRoles(queryPageBean));
    }

    @ApiOperation(value = "获取后台全部角色数据")
    @GetMapping("/admin/listAllRoles")
    public Result listAllRoles() {
        return Result.ok("获取后台角色数据", roleService.listAllRoles());
    }

    /**
     * 保存或更新角色
     *
     * @param roleVO 角色信息
     * @return {@link Result}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新角色信息")
    @PostMapping("/admin/saveOrUpdateRole")
    public Result saveOrUpdateRole(@RequestBody @Valid RoleVO roleVO) {
        roleService.saveOrUpdateRole(roleVO);
        return Result.ok("编辑角色信息成功");
    }

    /**
     * 删除角色
     *
     * @param roleIdList 角色id列表
     * @return {@link Result}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/admin")
    public Result deleteRoles(@RequestBody List<Integer> roleIdList) {
        roleService.deleteRoles(roleIdList);
        return Result.ok("删除角色成功");
    }

    /**
     * 修改用户角色
     *
     * @param userRoleVO 用户角色信息
     * @return {@link Result}
     */
    @ApiOperation(value = "修改用户角色")
    @PutMapping("/admin/user")
    public Result updateUserRole(@Valid @RequestBody UserRoleVO userRoleVO) {
        roleService.updateUserRole(userRoleVO);
        return Result.ok("修改用户角色成功");
    }

}
