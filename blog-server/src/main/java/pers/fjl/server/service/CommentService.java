package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.po.Comment;

import java.util.List;

/**
 * <p>
 * 评论服务类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
public interface CommentService extends IService<Comment> {

    /**
     * 获取评论信息的列表
     *
     * @param blogId
     * @return
     */
    List<Comment> getCommentList(Long blogId);

    /**
     * 回复评论
     *
     * @param comment
     * @param uid
     */
    void replyComment(Comment comment, Long uid);

    /**
     * 删除评论
     * @param blogId
     * @param commentId
     * @param uid
     */
    void delComment(Long blogId, Long commentId, Long uid);
}
