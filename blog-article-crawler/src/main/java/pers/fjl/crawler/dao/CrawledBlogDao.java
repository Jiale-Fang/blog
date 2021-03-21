package pers.fjl.crawler.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import pers.fjl.crawler.po.CrawledBlog;

@Repository
public interface CrawledBlogDao extends BaseMapper<CrawledBlog> {
}
