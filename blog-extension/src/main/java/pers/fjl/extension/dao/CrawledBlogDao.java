package pers.fjl.extension.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import pers.fjl.extension.po.CrawledBlog;

@Repository
public interface CrawledBlogDao extends BaseMapper<CrawledBlog> {
}
