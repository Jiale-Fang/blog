package pers.fjl.server.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.dto.LabelOptionDTO;
import pers.fjl.common.dto.MenuDTO;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.User;
import pers.fjl.common.vo.MenuVO;
import pers.fjl.server.annotation.LoginRequired;
import pers.fjl.server.annotation.OptLog;
import pers.fjl.server.service.MenuService;
import pers.fjl.server.service.ResourceService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static pers.fjl.common.constant.OptTypeConst.REMOVE;
import static pers.fjl.common.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * 资源模块控制器
 *
 * @author fangjiale 2022年01月14日
 */

@Api(value = "菜单模块", description = "菜单模块的接口信息")
@RequestMapping("/menu")
@RestController
@CrossOrigin
public class MenuController {
    @Resource
    private MenuService menuService;

    @LoginRequired
    @GetMapping("/admin/listUserMenus")
    @ApiOperation(value = "获取用户菜单列表")
    public Result listUserMenus(HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        return Result.ok("获取菜单列表成功", menuService.listUserMenus(user.getUid()));
    }

    @LoginRequired
    @GetMapping("/admin/listAdminMenus")
    @ApiOperation(value = "获取管理员菜单列表")
    public Result listAdminMenus(HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        return Result.ok("获取菜单列表成功", menuService.listAdminMenus(user.getUid()));
    }

    @GetMapping("/admin/listMenus")
    @ApiOperation(value = "获取后台菜单列表")
    public Result listMenus() {
        return Result.ok("获取后台菜单列表成功", menuService.listMenus());
    }

    /**
     * 查看角色菜单选项
     *
     * @return {@link Result} 查看角色菜单选项
     */
    @ApiOperation(value = "查看角色菜单选项")
    @GetMapping("/admin/role")
    public Result listMenuOptions() {
        return Result.ok("查看角色菜单选项", menuService.listMenuOptions());
    }

    /**
     * 新增或修改菜单
     *
     * @param menuVO 菜单
     * @return {@link Result}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "新增或修改菜单")
    @PostMapping("/admin/saveOrUpdateMenu")
    public Result saveOrUpdateMenu(@Valid @RequestBody MenuVO menuVO) {
        menuService.saveOrUpdateMenu(menuVO);
        return Result.ok("新增或更新菜单成功");
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     * @return {@link Result}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/admin/delete/{menuId}")
    public Result deleteMenu(@PathVariable("menuId") Integer menuId){
        menuService.deleteMenu(menuId);
        return Result.ok("删除菜单成功");
    }
}
