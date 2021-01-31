package pers.fjl.server.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
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

    @GetMapping("/{blogId}")
    public Result getCommentList(@PathVariable("blogId") Long blogId) {
        return new Result(true, MessageConstant.OK, "获取评论列表信息成功", commentService.getCommentList(blogId));
    }

    @LoginRequired
    @PostMapping("/replyComment")
    public Result replyComment(@RequestBody Comment comment, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        commentService.replyComment(comment, user.getUid());
        return new Result(true, "回复评论信息成功", MessageConstant.OK);
    }

    @LoginRequired
    @DeleteMapping("/{blogId}/{commentId}")
    public Result delComment(@PathVariable Long blogId, @PathVariable Long commentId, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        commentService.delComment(blogId, commentId, user.getUid());
        return new Result(true, "删除成功", MessageConstant.OK);
    }

}
