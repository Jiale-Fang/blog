package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Type;
import pers.fjl.common.vo.TypeVo;

import java.util.List;

/**
 * <p>
 * 分类服务类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
public interface TypeService extends IService<Type> {
    /**
     * 存储type
     *
     * @param type
     * @return type
     */
    boolean addType(Type type);

    /**
     * 获取type
     *
     * @param id
     * @return type
     */
    Type getType(Long id);

    /**
     * 后台管理分类列表
     *
     * @param queryPageBean
     * @return Page
     */
    Page<Type> findPage(QueryPageBean queryPageBean);

    /**
     * 更新分类
     *
     * @param id
     * @param type
     * @return type
     */
    boolean updateType(Long id, Type type);

    /**
     * 删除分类
     *
     * @param id
     * @return boolean
     */
    boolean delete(Long id);

    /**
     * 获取type列表回显
     * @return list
     */
    List<Type> getTypeList();

    /**
     * 获取每个分类的博客数量
     * @return list
     */
    List<TypeVo> getTypeCount();

}
