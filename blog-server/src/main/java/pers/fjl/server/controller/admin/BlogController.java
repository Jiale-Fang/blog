package pers.fjl.server.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.User;
import pers.fjl.common.vo.AddBlogVo;
import pers.fjl.server.annotation.LoginRequired;
import pers.fjl.server.service.BlogService;

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

    @LoginRequired
    @ApiOperation(value = "分页查询", notes = "返回分页数据")
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        return new Result(true, MessageConstant.OK, "获取分页数据成功", blogService.findPage(queryPageBean, user.getUid()));
    }

    @LoginRequired
    @PostMapping("/add")
    public Result addType(@RequestBody AddBlogVo addBlogVo, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        boolean flag = blogService.addBlog(addBlogVo, user.getUid());
        return new Result(flag, "添加成功", MessageConstant.OK);
    }

//    @PutMapping("/{id}/state/{dataStatus}")
//    public Result userStateChanged(@PathVariable("id") String id,
//                                   @PathVariable("dataStatus") boolean dataStatus) {

    @GetMapping("/{blogId}")
    public Result getOneBlog(@PathVariable("blogId") Long blogId){
        blogService.setViews(blogId);
        return new Result(true, MessageConstant.OK, "获取博客信息成功", blogService.getOneBlog(blogId));
    }

}
