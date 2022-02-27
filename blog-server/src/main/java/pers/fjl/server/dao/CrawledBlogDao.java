package pers.fjl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import pers.fjl.common.po.CrawledBlog;

@Repository
public interface CrawledBlogDao extends BaseMapper<CrawledBlog> {
}
