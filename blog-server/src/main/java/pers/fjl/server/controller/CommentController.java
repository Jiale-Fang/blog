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
import pers.fjl.server.annotation.OptLog;
import pers.fjl.server.service.CommentService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static pers.fjl.common.constant.OptTypeConst.REMOVE;


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
        return Result.ok("获取评论列表信息成功", commentService.getCommentList(blogId));
    }

    @ApiOperation(value = "回复评论")
    @LoginRequired
    @PostMapping("/admin/replyComment")
    public Result replyComment(@RequestBody Comment comment, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        commentService.replyComment(comment, user.getUid());
        return Result.ok("回复评论信息成功");
    }

    @LoginRequired
    @ApiOperation(value = "用户删除评论")
    @DeleteMapping("/admin/del/{blogId}/{commentId}")
    public Result delComment(@PathVariable Long blogId, @PathVariable Long commentId, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        if (commentService.delComment(blogId, commentId, user.getUid()))
            return Result.ok("删除成功");
        return Result.fail("您删除的评论不是你发布的，你无权删除！");
    }

    @ApiOperation(value = "获取后台的评论分页数据")
    @PostMapping("/admin/commentPage")
    public Result adminComments(@RequestBody QueryPageBean queryPageBean){
        return Result.ok("获取分页数据成功",commentService.adminComments(queryPageBean));
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除评论")
    @DeleteMapping("/admin/delete")
    public Result adminDelComment(@RequestBody List<Long> commentIdList) {
        commentService.removeByIds(commentIdList);
        return Result.ok("删除评论成功");
    }

}
