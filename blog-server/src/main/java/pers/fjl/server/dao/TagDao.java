package pers.fjl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import pers.fjl.common.po.Tag;
import pers.fjl.common.vo.TagVo;
import pers.fjl.common.vo.TypeVo;

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
public interface TagDao extends BaseMapper<Tag> {
    /**
     * 获取每个标签的博客数量
     * @return list
     */
    @Select("SELECT DISTINCT t.tag_id, t.tag_name, COUNT(bts.tag_id) tag_count " +
            "FROM tag t LEFT OUTER JOIN blog_tag bts " +
            "ON t.tag_id = bts.tag_id " +
            "GROUP BY t.tag_id " +
            "ORDER BY COUNT(bts.tag_id) DESC ")
    List<TagVo> getTagCount();
}
