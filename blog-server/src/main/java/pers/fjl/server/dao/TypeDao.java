package pers.fjl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import pers.fjl.common.po.Type;
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
public interface TypeDao extends BaseMapper<Type> {

    /**
     * 获取每个分类的博客数量
     * @return
     */
    @Select("SELECT DISTINCT t.type_id, t.type_name, COUNT(b.type_id) type_count " +
            "FROM type t LEFT OUTER JOIN blog b " +
            "ON t.type_id = b.type_id " +
            "GROUP BY t.type_id " +
            "ORDER BY COUNT(b.type_id) DESC")
    List<TypeVo> getTypeCount();
}
