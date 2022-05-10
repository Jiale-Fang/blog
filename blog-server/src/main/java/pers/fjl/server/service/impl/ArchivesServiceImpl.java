package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pers.fjl.common.po.Blog;
import pers.fjl.server.dao.BlogDao;
import pers.fjl.server.service.ArchivesService;

import javax.annotation.Resource;

@Service
public class ArchivesServiceImpl implements ArchivesService {
    @Resource
    private BlogDao blogDao;

    @Cacheable(value = {"BlogPage"}, key = "#root.methodName")
    public Page<Blog> getArchivesList() {
        Page<Blog> page = new Page<>(1, 100);
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.select("blog_id", "title", "create_time", "first_picture")
                .orderByDesc("create_time");
        return blogDao.selectPage(page, wrapper);
    }
}
