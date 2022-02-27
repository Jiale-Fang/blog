package pers.fjl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import pers.fjl.common.entity.QueryPageBean;
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
     * @return 分类数据
     */
    List<TypeVo> getTypeCount();

    /**
     * 获取后台管理分页数据
     * @param queryPageBean 分页实体
     * @return list
     */
    List<TypeVo> adminType(@Param("queryPageBean") QueryPageBean queryPageBean);

}
