package pers.fjl.extension.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import pers.fjl.extension.po.BlogInfo;

public interface BlogInfoMapper extends ElasticsearchRepository<BlogInfo, String> {
}
