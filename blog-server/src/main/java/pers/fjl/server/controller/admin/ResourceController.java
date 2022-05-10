package pers.fjl.server.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.common.vo.ResourceVO;
import pers.fjl.server.annotation.OptLog;
import pers.fjl.server.service.ResourceService;

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

@Api(value = "资源管理模块", description = "资源管理模块的接口信息")
@RequestMapping("/resource")
@RestController
@CrossOrigin
public class ResourceController {
    @Resource
    private ResourceService resourceService;

    @GetMapping("/list")
    @ApiOperation(value = "查询所有资源")
    public List<String> getResourceList() {
        return resourceService.getResourceList();
    }

    @GetMapping("/getUserResource")
    @ApiOperation(value = "查询用户权限")
    public List<String> getUserResource(String uid) {
        return resourceService.getUserResource(Long.parseLong(uid));
    }

    /**
     * 查看角色菜单选项
     *
     * @return {@link Result} 查看角色菜单选项
     */
    @ApiOperation(value = "查看角色菜单选项")
    @GetMapping("/role")
    public Result listResourceOptions() {
        return Result.ok("查看角色菜单选项", resourceService.listResourceOptions());
    }

    /**
     * 查看资源列表
     *
     * @return {@link Result} 资源列表
     */
    @ApiOperation(value = "查看资源列表")
    @GetMapping("/listResources")
    public Result listResources(QueryPageBean queryPageBean){
        return Result.ok("查看角色菜单选项", resourceService.listResources(queryPageBean));
    }

    /**
     * 新增或修改资源
     *
     * @param resourceVO 资源信息
     * @return {@link Result}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "新增或修改资源")
    @PostMapping("/admin/saveOrUpdateResource")
    public Result saveOrUpdateResource(@RequestBody @Valid ResourceVO resourceVO) {
        resourceService.saveOrUpdateResource(resourceVO);
        return Result.ok("新增或更新资源成功");
    }

    /**
     * 删除资源
     *
     * @param resourceId 资源id
     * @return {@link Result}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除资源")
    @DeleteMapping("/admin/delete/{resourceId}")
    public Result deleteResource(@PathVariable("resourceId") Integer resourceId) {
        resourceService.deleteResource(resourceId);
        return Result.ok("删除资源成功");
    }
}
