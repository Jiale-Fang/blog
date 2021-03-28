package pers.fjl.extension.service;

import pers.fjl.common.entity.PageResult;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.extension.po.BlogInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BlogInfoService {

    /**
     * 导入数据到索引库中
     */
    void importData();

    /**
     * 保存数据
     * @param blogInfo
     */
    void save(BlogInfo blogInfo);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     * @throws IOException
     */
    PageResult homePage(QueryPageBean queryPageBean) throws IOException;

    /**
     * 用es实现搜索功能
     * @param queryPageBean
     * @return  list
     * @throws IOException
     */
    List<Map<String, Object>> searchPage(QueryPageBean queryPageBean) throws IOException;

    /**
     * 用es实现高亮搜索功能
     * @param queryPageBean
     * @return list
     * @throws IOException
     */
    PageResult highLightSearchPage(QueryPageBean queryPageBean) throws IOException;

}
