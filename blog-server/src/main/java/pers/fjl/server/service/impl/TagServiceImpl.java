package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Blog;
import pers.fjl.common.po.Tag;
import pers.fjl.common.po.Type;
import pers.fjl.common.vo.BlogVo;
import pers.fjl.common.vo.TagVo;
import pers.fjl.server.dao.BlogTagDao;
import pers.fjl.server.dao.TagDao;
import pers.fjl.server.service.TagService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagDao, Tag> implements TagService {
    @Resource
    private TagDao tagDao;

    public List<Tag> getTagList() {
        return tagDao.selectList(null);
    }

    @Cacheable(value = {"BlogPage"}, key = "#root.methodName")
    public List<TagVo> getTagCount() {
        return tagDao.getTagCount();
    }

    @Override
    public Page<Tag> findPage(QueryPageBean queryPageBean) {
        //设置分页条件
        Page<Tag> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //设置查询条件
        QueryWrapper<Tag> wrapper = new QueryWrapper<>();
//        wrapper.like(queryPageBean.getQueryString() != null, "role_name", queryPageBean.getQueryString());
        return tagDao.selectPage(page, wrapper);
    }

}
