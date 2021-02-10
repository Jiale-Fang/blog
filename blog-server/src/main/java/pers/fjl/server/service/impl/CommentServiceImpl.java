package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pers.fjl.common.po.Comment;
import pers.fjl.common.po.User;
import pers.fjl.server.dao.CommentDao;
import pers.fjl.server.service.CommentService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 评论服务服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {
    @Resource
    private CommentDao commentDao;

    @Cacheable(value = {"CommentMap"}, key = "#blogId")
    public List<Comment> getCommentList(Long blogId) {
        // 获取一级评论的list
        List<Comment> comments1 = commentDao.selectRootList(blogId);
        // 获取二级评论的list
        List<Comment> comments2 = commentDao.selectChildList(blogId);
        return combineChildren(comments1, comments2);
    }

    @CacheEvict(value = {"CommentMap"}, key = "#comment.blogId")
    public void replyComment(Comment comment, Long uid) {
        comment.setUid(uid);
        if (comment.getParentCommentId() == null) {
            comment.setParentCommentId(-1L);
        }
        commentDao.insert(comment);
    }

    @CacheEvict(value = {"CommentMap"}, key = "#blogId")
    public void delComment(Long blogId, Long commentId, Long uid) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid)
                .eq("blog_id", blogId)
                .eq("comment_id", commentId);
        if (commentDao.selectOne(wrapper) == null) {
            throw new RuntimeException("您删除的评论不是你发布的，你无权删除！");
        }
        // 获取被删的根评论
        Comment comment = commentDao.selectOne(wrapper);
        // 递归删除该评论以及其子评论
        List<Long> idList = new ArrayList<>();
        getDelIdList(commentId, idList);
        idList.add(commentId);
        commentDao.deleteBatchIds(idList);
    }

    /**
     * 获取递归删除的id的list
     */
    public void getDelIdList(Long commentId, List<Long> idList) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_comment_id",commentId)
                .select("comment_id");
        List<Comment> childIdList = commentDao.selectList(wrapper);
        //把childIdList里面菜单id值获取出来，封装idList里面，做递归查询
        childIdList.stream().forEach(item -> {
            //封装idList里面
            idList.add(item.getCommentId());
            //递归查询
            this.getDelIdList(item.getCommentId(),idList);
        });
    }

    /**
     * 把子评论加入到父评论的children
     *
     * @param rootList
     * @param childList
     * @return list
     */
    public List<Comment> combineChildren(List<Comment> rootList, List<Comment> childList) {
        for (Comment root : rootList) {
            List<Comment> comments = new ArrayList<>();
            for (Comment child : childList) {
                if (child.getParentCommentId().equals(root.getCommentId())) {
                    comments.add(child);
                }
            }
            List<Comment> comments1 = combineChildren(comments, childList);
            root.setChildren(comments1);
        }
        return rootList;
    }
}
