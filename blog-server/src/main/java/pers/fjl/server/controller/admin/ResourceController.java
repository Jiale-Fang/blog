package pers.fjl.server.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.dto.LabelOptionDTO;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.User;
import pers.fjl.common.vo.AddBlogVo;
import pers.fjl.server.annotation.IpRequired;
import pers.fjl.server.annotation.LoginRequired;
import pers.fjl.server.config.RabbitConfig;
import pers.fjl.server.search.mq.PostMqIndexMessage;
import pers.fjl.server.service.BlogService;
import pers.fjl.server.service.ResourceService;
import pers.fjl.server.service.ViewsService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
     * @return {@link Result< LabelOptionDTO >} 查看角色菜单选项
     */
    @ApiOperation(value = "查看角色菜单选项")
    @GetMapping("/role")
    public Result listResourceOptions() {
        return new Result(true, MessageConstant.OK, "查看角色菜单选项", resourceService.listResourceOptions());
    }

    /**
     * 查看资源列表
     *
     * @return {@link Result} 资源列表
     */
    @ApiOperation(value = "查看资源列表")
    @GetMapping("/listResources")
    public Result listResources(QueryPageBean queryPageBean){
        return new Result(true, MessageConstant.OK, "查看角色菜单选项", resourceService.listResources(queryPageBean));
    }
}
