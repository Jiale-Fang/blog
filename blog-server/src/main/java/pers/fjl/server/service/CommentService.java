package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Comment;
import pers.fjl.common.vo.CommentVO;

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
    List<CommentVO> getCommentList(Long blogId);

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
    boolean delComment(Long blogId, Long commentId, Long uid);

    /**
     * 获取后台评论的分页数据
     * @param queryPageBean 分页实体
     * @return page
     */
    Page<CommentVO> adminComments(QueryPageBean queryPageBean);
}
