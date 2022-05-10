package pers.fjl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Tag;
import pers.fjl.common.vo.TagVO;

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
    List<TagVO> getTagCount();

    /**
     * 获取单个博客的tagList
     * @param blogId
     * @return
     */
    List<Tag> getBlogTagList(@Param("blogId") Long blogId);

    /**
     * 获取后台管理标签的分页数据
     * @return list
     */
    List<TagVO> adminTag(@Param("queryPageBean") QueryPageBean queryPageBean);
}
