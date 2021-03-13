package pers.fjl.search.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import pers.fjl.search.pojo.BlogInfo;

public interface BlogInfoMapper extends ElasticsearchRepository<BlogInfo, String> {
}
