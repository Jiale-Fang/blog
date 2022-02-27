package pers.fjl.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.Comment;
import pers.fjl.common.po.User;
import pers.fjl.server.annotation.LoginRequired;
import pers.fjl.server.service.CommentService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 评论模块
 *
 * @author fangjiale 2021年01月30日
 */

@Api(value = "评论模块", description = "评论模块的接口信息")
@RequestMapping("/comment")
@RestController
@CrossOrigin
public class CommentController {
    @Resource
    private CommentService commentService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "blogId", value = "博客id", type = "Long", required = true)
    })
    @ApiOperation(value = "根据blogId查找评论")
    @GetMapping("/commentList/{blogId}")
    public Result getCommentList(@PathVariable("blogId") Long blogId) {
        return new Result(true, MessageConstant.OK, "获取评论列表信息成功", commentService.getCommentList(blogId));
    }

    @ApiOperation(value = "回复评论")
    @LoginRequired
    @PostMapping("/replyComment")
    public Result replyComment(@RequestBody Comment comment, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        commentService.replyComment(comment, user.getUid());
        return new Result(true, "回复评论信息成功", MessageConstant.OK);
    }

    @LoginRequired
    @ApiOperation(value = "删除评论")
    @DeleteMapping("/del/{blogId}/{commentId}")
    public Result delComment(@PathVariable Long blogId, @PathVariable Long commentId, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        if (commentService.delComment(blogId, commentId, user.getUid()))
            return new Result(true, "删除成功", MessageConstant.OK);
        return new Result(false, "您删除的评论不是你发布的，你无权删除！", MessageConstant.ERROR);
    }

    @ApiOperation(value = "获取后台的评论分页数据")
    @PostMapping("/adminComments")
    public Result adminComments(@RequestBody QueryPageBean queryPageBean){
        return new Result(true, MessageConstant.OK, "获取分页数据成功",commentService.adminComments(queryPageBean));
    }

}
