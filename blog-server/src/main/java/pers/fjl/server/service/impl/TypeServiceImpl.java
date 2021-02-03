package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Type;
import pers.fjl.common.po.User;
import pers.fjl.common.vo.TypeVo;
import pers.fjl.server.dao.TypeDao;
import pers.fjl.server.service.TypeService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  分类服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
@Service
public class TypeServiceImpl extends ServiceImpl<TypeDao, Type> implements TypeService {

    @Resource
    private TypeDao typeDao;

    @Override
    public boolean addType(Type type) {
        if (typeExist(type.getTypeName())){ // 要添加的分类已存在
            throw new RuntimeException("添加失败，要添加的分类已存在");
        }
        typeDao.insert(type);
        return true;
    }

    public boolean typeExist(String typeName){
        QueryWrapper<Type> wrapper = new QueryWrapper<>();
        wrapper.select("1");
        wrapper.eq("type_name", typeName).last("limit 1");
        return typeDao.selectCount(wrapper) != 0;
    }

    @Override
    public Type getType(Long id) {
        return null;
    }

    public Page<Type> findPage(QueryPageBean queryPageBean) {
        //设置分页条件
        Page<Type> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //设置查询条件
        QueryWrapper<Type> wrapper = new QueryWrapper<>();
//        wrapper.like(queryPageBean.getQueryString() != null, "role_name", queryPageBean.getQueryString());
        return typeDao.selectPage(page, wrapper);
    }

    @Override
    public boolean updateType(Long id, Type type) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public List<Type> getTypeList() {
        return typeDao.selectList(null);
    }

    @Cacheable(value = {"BlogPage"}, key = "#root.methodName")
    public List<TypeVo> getTypeCount() {
        return typeDao.getTypeCount();
    }
}
