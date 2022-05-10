package pers.fjl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Comment;
import pers.fjl.common.vo.CommentVO;

import java.util.List;

/**
 * <p>
 *  评论dao接口
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
@Repository
public interface CommentDao extends BaseMapper<Comment> {

    /**
     * 将根节点评论封装到list
     * @return
     */
//    @Select("SELECT u.nickname, u.avatar, c.comment_id, c.uid, c.content, c.create_time, c.blog_id, c.parent_comment_id " +
//            "FROM comment c, user u " +
//            "WHERE c.uid = u.uid AND c.blog_id = #{blogId} AND c.parent_comment_id = -1 ")
    List<CommentVO> selectRootList(Long blogId);

    /**
     * 将不是根节点评论封装到list
     * @return
     */
//    @Select("SELECT u.nickname, u.avatar, c.comment_id, c.uid, c.content, c.create_time, c.blog_id, c.parent_comment_id, c.reply_uid, uu.nickname as reply_nickname " +
//            "FROM comment c, user u, user uu " +
//            "WHERE c.uid = u.uid AND c.blog_id = #{blogId} AND c.parent_comment_id != -1 AND c.reply_uid = uu.uid")
    List<CommentVO> selectChildList(Long blogId);

    /**
     * 获取分页数据
     * @param queryPageBean 实体
     * @return list
     */
    List<CommentVO> adminComments(@Param("queryPageBean") QueryPageBean queryPageBean);
}
