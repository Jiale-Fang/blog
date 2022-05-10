package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Blog;
import pers.fjl.common.po.Type;
import pers.fjl.common.vo.TypeVO;
import pers.fjl.server.dao.BlogDao;
import pers.fjl.server.dao.TypeDao;
import pers.fjl.server.exception.BizException;
import pers.fjl.server.service.TypeService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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
    @Resource
    private BlogDao blogDao;

    @Override
    public boolean addType(Type type) {
        if (typeExist(type.getTypeName())){ // 要添加的分类已存在
            return false;
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
    public List<TypeVO> getTypeCount() {
        return typeDao.getTypeCount();
    }

    @Override
    public Page<TypeVO> adminType(QueryPageBean queryPageBean) {
        //设置分页条件
        Page<TypeVO> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<Type> wrapper = new QueryWrapper<>();
        wrapper.like(queryPageBean.getQueryString() != null, "type_name", queryPageBean.getQueryString());
        page.setTotal(typeDao.selectCount(wrapper));
        page.setRecords(typeDao.adminType(queryPageBean));
        return page;
    }

    @Override
    public List<Type> searchTypes(QueryPageBean queryPageBean) {
        // 搜索标签
        return typeDao.selectList(new LambdaQueryWrapper<Type>()
                .like(StringUtils.isNotBlank(queryPageBean.getQueryString()), Type::getTypeName, queryPageBean.getQueryString())
                .orderByDesc(Type::getTypeId));
    }

    @Override
    @Transactional
    public boolean saveOrUpdateType(Type type) {
        //判断分类是否存在
        Type typeDB = typeDao.selectOne(new LambdaQueryWrapper<Type>().eq(Type::getTypeName, type.getTypeName())
                .select(Type::getTypeId));
        if (Objects.nonNull(typeDB)) {   // 分类已经存在
            throw new BizException("分类名已存在");
        }
        Type typeAdd = Type.builder()
                .typeId(type.getTypeId())
                .typeName(type.getTypeName())
                .build();
        this.saveOrUpdate(typeAdd);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Integer> typeIdList) {
        // 查询分类id下是否有文章
        Integer count = blogDao.selectCount(new LambdaQueryWrapper<Blog>()
                .in(Blog::getTypeId, typeIdList));
        if (count > 0) {
            throw new BizException("删除失败，分类中存在文章");
        }
        typeDao.deleteBatchIds(typeIdList);
    }
}
