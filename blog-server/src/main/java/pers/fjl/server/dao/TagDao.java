package pers.fjl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import pers.fjl.common.entity.QueryPageBean;
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
    List<TagVo> getTagCount();

    /**
     * 获取后台管理标签的分页数据
     * @return list
     */
    List<TagVo> adminTag(@Param("queryPageBean") QueryPageBean queryPageBean);
}
