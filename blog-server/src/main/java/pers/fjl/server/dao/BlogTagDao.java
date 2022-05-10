package pers.fjl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import pers.fjl.common.po.BlogTag;
import pers.fjl.common.vo.BlogVO;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
@Repository
public interface BlogTagDao extends BaseMapper<BlogTag> {
    /**
     * 根据标签类型获取博客列表
     * @param start
     * @param pageSize
     * @return
     */
    @Select("SELECT b.blog_id, u.nickname, u.avatar, type.type_name, t.tag_name, b.views, b.description, b.create_time ,b.recommend, b.published, b.update_time, b.title, b.first_picture " +
            "FROM blog b, user u, tag t, blog_tag bts, type " +
            "WHERE b.uid = u.uid AND b.blog_id = bts.blog_id AND bts.tag_id = t.tag_id AND t.tag_id = #{tagId} AND type.type_id = b.type_id " +
            "GROUP BY b.blog_id " +
            "ORDER BY b.views DESC " +
            "LIMIT #{start},#{pageSize}")
    List<BlogVO> getByTagId(Integer start, Integer pageSize, Integer tagId);
}
