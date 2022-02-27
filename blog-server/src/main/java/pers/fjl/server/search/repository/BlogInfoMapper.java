package pers.fjl.server.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import pers.fjl.server.search.index.BlogInfo;

/**
 * 接口符合JPA规范，可简写
 */
@Repository
public interface BlogInfoMapper extends ElasticsearchRepository<BlogInfo, Long> {
}
