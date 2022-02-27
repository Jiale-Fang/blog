package pers.fjl.server.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.dto.BlogBackInfoDTO;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.User;
import pers.fjl.common.vo.AddBlogVo;
import pers.fjl.server.annotation.IpRequired;
import pers.fjl.server.annotation.LoginRequired;
import pers.fjl.server.config.RabbitConfig;
import pers.fjl.server.search.mq.PostMqIndexMessage;
import pers.fjl.server.service.BlogService;
import pers.fjl.server.service.ViewsService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 分类管理模块
 *
 * @author fangjiale 2021年01月27日
 */

@Api(value = "博客管理模块", description = "博客管理模块的接口信息")
@RequestMapping("/blog")
@RestController
@CrossOrigin
public class BlogController {
    @Resource
    private BlogService blogService;
    @Resource
    private ViewsService viewsService;
    @Resource
    private AmqpTemplate amqpTemplate;

    @LoginRequired
    @ApiOperation(value = "个人后台分页查询", notes = "返回分页数据")
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        return new Result(true, MessageConstant.OK, "获取分页数据成功", blogService.findPage(queryPageBean, user.getUid()));
    }

    @LoginRequired
    @ApiOperation(value = "用户添加博客")
    @PostMapping("/add")
    public Result addBlog(@RequestBody AddBlogVo addBlogVo, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        boolean flag = blogService.addBlog(addBlogVo, user.getUid());
        //发消息给mq然后同步es
        amqpTemplate.convertAndSend(RabbitConfig.esExchange, RabbitConfig.esBingKey,
                new PostMqIndexMessage(addBlogVo.getBlogId(), PostMqIndexMessage.CREATE_OR_UPDATE));
        return new Result(flag, "添加成功", MessageConstant.OK);
    }

    @IpRequired
    @ApiOperation(value = "根据id获取博客的信息")
    @GetMapping("/getById/{blogId}")
    public Result getOneBlog(@PathVariable("blogId") Long blogId, HttpServletRequest request) {
        viewsService.addViews(blogId, (String) request.getAttribute("host"));
        blogService.setViews(blogId);
        return new Result(true, MessageConstant.OK, "获取博客信息成功", blogService.getOneBlog(blogId));
    }

    @GetMapping("/thumbUp/{blogId}/{uid}")
    public Result thumbsUp(@PathVariable("blogId") Long blogId, @PathVariable("uid") Long uid) {
        if (blogService.thumbsUp(blogId, uid))
            return new Result(true, "点赞成功", MessageConstant.OK);
        return new Result(false, "取消点赞成功", MessageConstant.ERROR);
    }

    /**
     * 查看后台信息
     *
     * @return 后台信息
     */
    @ApiOperation(value = "查看后台信息")
    @GetMapping("/admin")
    public Result getBlogBackInfo() {
        return new Result(true, MessageConstant.OK, "获取后台信息成功", blogService.getBlogBackInfo());
    }

    /**
     * 查看后台信息
     *
     * @return 后台信息
     */
    @ApiOperation(value = "后台获取博客信息")
    @GetMapping("/adminBlogPage")
    public Result adminBlogPage(QueryPageBean queryPageBean) {
        return new Result(true, MessageConstant.OK, "获取后台信息成功", blogService.adminBlogPage(queryPageBean));
    }

}
