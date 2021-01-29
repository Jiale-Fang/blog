package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.fjl.common.po.BlogTag;
import pers.fjl.server.dao.BlogTagDao;
import pers.fjl.server.service.BlogTagService;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagDao, BlogTag> implements BlogTagService {
    @Resource
    private BlogTagDao blogTagDao;

    @Transactional
    public boolean addOneBlogTag(Long blogId, Long[] value) {
        BlogTag blogTag = new BlogTag();
        blogTag.setBlogId(blogId);
        for (Long tag : value) {
            blogTag.setTagId(tag);
            blogTagDao.insert(blogTag);
        }
        return true;
    }
}
