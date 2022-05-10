package pers.fjl.server.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.RabbitMQConst;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.User;
import pers.fjl.common.vo.AddBlogVO;
import pers.fjl.common.vo.BlogVO;
import pers.fjl.server.annotation.LoginRequired;
import pers.fjl.server.annotation.OptLog;
import pers.fjl.server.search.mq.PostMqIndexMessage;
import pers.fjl.server.service.BlogService;
import pers.fjl.server.service.ViewsService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static pers.fjl.common.constant.OptTypeConst.REMOVE;

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
    @Resource
    private RabbitTemplate rabbitTemplate;

    @LoginRequired
    @ApiOperation(value = "个人后台分页查询", notes = "返回分页数据")
    @PostMapping("/admin/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        return Result.ok("获取分页数据成功", blogService.findPage(queryPageBean, user.getUid()));
    }

    @LoginRequired
    @ApiOperation(value = "用户添加或更新博客")
    @PostMapping("/admin/addOrUpdate")
    public Result addOrUpdate(@RequestBody AddBlogVO addBlogVO, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        Long blogId = blogService.addOrUpdateBlog(addBlogVO, user.getUid());
        System.out.println("es测试" + blogId);
        //发消息给mq然后同步es
        amqpTemplate.convertAndSend(RabbitMQConst.esExchange, RabbitMQConst.esBingKey,
                new PostMqIndexMessage(blogId, PostMqIndexMessage.CREATE_OR_UPDATE));
        return Result.ok("添加或更新博客成功");
    }

    @LoginRequired
    @ApiOperation(value = "管理员更新或发布博客")
    @PostMapping("/admin/saveOrUpdate")
    public Result adminSaveOrUpdateBlog(@RequestBody BlogVO blogVO, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        blogService.adminSaveOrUpdateBlog(blogVO, user.getUid());
        return Result.ok("编辑成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "管理员删除博客")
    @DeleteMapping("/admin/delete")
    public Result deleteBlogs(@RequestBody List<Long> blogIdList) {
        blogService.deleteBlogs(blogIdList);
        PostMqIndexMessage postMqIndexMessage = new PostMqIndexMessage();
        postMqIndexMessage.setBlogIdList(blogIdList).setType(PostMqIndexMessage.REMOVE);
        amqpTemplate.convertAndSend(RabbitMQConst.esExchange, RabbitMQConst.esBingKey, postMqIndexMessage);
        return Result.ok("删除博客成功");
    }

    @ApiOperation(value = "根据id获取博客的信息")
    @GetMapping("/{blogId}")
    public Result getOneBlog(@PathVariable("blogId") Long blogId) {
        blogService.updateBlogViewsCount(blogId);
        return Result.ok("获取博客信息成功", blogService.getOneBlog(blogId));
    }

    @ApiOperation(value = "点赞")
    @GetMapping("/admin/thumbUp/{blogId}/{uid}")
    public Result thumbsUp(@PathVariable("blogId") Long blogId, @PathVariable("uid") Long uid) {
        boolean flag = blogService.thumbsUp(blogId, uid);
        if (flag)
            return Result.ok("点赞成功");
        return Result.ok("取消点赞成功");
    }

    @ApiOperation(value = "收藏")
    @GetMapping("/admin/favorite/{blogId}/{uid}")
    public Result favorite(@PathVariable("blogId") Long blogId, @PathVariable("uid") Long uid) {
        boolean flag = blogService.favorite(blogId, uid);
        if (flag)
            return Result.ok("收藏成功");
        return Result.ok("取消收藏成功");
    }

    @PostMapping("/search")
    public Result search(@RequestBody QueryPageBean queryPageBean) {
        return Result.ok("查询成功", blogService.search(queryPageBean));
    }

    /**
     * 查看后台信息
     *
     * @return 后台信息
     */
    @ApiOperation(value = "查看后台信息")
    @GetMapping("/admin/getBlogBackInfo")
    public Result getBlogBackInfo() {
        return Result.ok("获取后台信息成功", blogService.getBlogBackInfo());
    }

    /**
     * 查看后台信息
     *
     * @return 后台信息
     */
    @ApiOperation(value = "后台获取博客信息")
    @GetMapping("/admin/blogPage")
    public Result adminBlogPage(QueryPageBean queryPageBean) {
        return Result.ok("获取后台信息成功", blogService.adminBlogPage(queryPageBean));
    }

    @LoginRequired
    @ApiOperation(value = "收藏夹分页查询", notes = "返回分页数据")
    @PostMapping("/admin/findFavoritesPage")
    public Result findFavoritesPage(@RequestBody QueryPageBean queryPageBean, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        return Result.ok("获取分页数据成功", blogService.findFavoritesPage(queryPageBean, user.getUid()));
    }

    @ApiOperation(value = "博文信息", notes = "博文信息")
    @GetMapping("/blogInfo")
    public Result blogInfo() {
        return Result.ok("获取分页数据成功", blogService.blogInfo());
    }

}
