package pers.fjl.server.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.dto.LabelOptionDTO;
import pers.fjl.common.dto.MenuDTO;
import pers.fjl.common.entity.Result;
import pers.fjl.server.service.MenuService;
import pers.fjl.server.service.ResourceService;

import javax.annotation.Resource;
import java.util.List;

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

    @GetMapping("/listUserMenus")
    @ApiOperation(value = "获取菜单列表")
    public Result listUserMenus() {
        return new Result(true, MessageConstant.OK, "获取菜单列表成功", menuService.listUserMenus());
    }

    @GetMapping("/listMenus")
    @ApiOperation(value = "获取后台菜单列表")
    public Result listMenus() {
        return new Result(true, MessageConstant.OK, "获取后台菜单列表成功", menuService.listMenus());
    }

    /**
     * 查看角色菜单选项
     *
     * @return {@link Result< LabelOptionDTO >} 查看角色菜单选项
     */
    @ApiOperation(value = "查看角色菜单选项")
    @GetMapping("/role")
    public Result listMenuOptions() {
        return new Result(true,MessageConstant.OK, "查看角色菜单选项", menuService.listMenuOptions());
    }
}
