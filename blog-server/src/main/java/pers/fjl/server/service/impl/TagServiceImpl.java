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
import pers.fjl.common.po.BlogTag;
import pers.fjl.common.po.Tag;
import pers.fjl.common.vo.TagVO;
import pers.fjl.server.dao.BlogTagDao;
import pers.fjl.server.dao.TagDao;
import pers.fjl.server.exception.BizException;
import pers.fjl.server.service.TagService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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
    @Resource
    private BlogTagDao blogTagDao;

    public List<Tag> getTagList() {
        return tagDao.selectList(null);
    }

    @Cacheable(value = {"BlogPage"}, key = "#root.methodName")
    public List<TagVO> getTagCount() {
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

    @Override
    public Page<TagVO> adminTag(QueryPageBean queryPageBean) {
        //设置分页条件
        Page<TagVO> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<Tag> wrapper = new QueryWrapper<>();
        wrapper.like(queryPageBean.getQueryString() != null, "tag_name", queryPageBean.getQueryString());
        page.setTotal(tagDao.selectCount(wrapper));
        page.setRecords(tagDao.adminTag(queryPageBean));
        return page;
    }

    @Override
    public List<Tag> searchTags(QueryPageBean queryPageBean) {
        // 搜索标签
        return tagDao.selectList(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.isNotBlank(queryPageBean.getQueryString()), Tag::getTagName, queryPageBean.getQueryString())
                .orderByDesc(Tag::getTagId));
    }

    @Override
    @Transactional
    public boolean saveOrUpdateType(Tag tag) {
        Tag tagDB = tagDao.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getTagName, tag.getTagName()).select(Tag::getTagId));
        if (Objects.nonNull(tagDB)) {
            throw new BizException("标签已存在");
        }
        Tag tagAdd = Tag.builder()
                .tagId(tag.getTagId())
                .tagName(tag.getTagName())
                .build();
        this.saveOrUpdate(tagAdd);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Integer> tagIdList) {
        Integer count = blogTagDao.selectCount(new LambdaQueryWrapper<BlogTag>().in(BlogTag::getTagId, tagIdList));
        if (count > 0) {
            throw new BizException("删除失败，标签下存在文章");
        }
        tagDao.deleteBatchIds(tagIdList);
    }

}
