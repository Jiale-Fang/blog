package pers.fjl.server.service;

import java.util.Map;

public interface ReportService {

    /**
     * 获取某个用户的博文数据
     * @param uid
     * @return map
     */
    Map<String, Object> getReport(Long uid) throws Exception;

    /**
     * 获取单篇博文数据
     * @param uid
     * @return map
     */
    Map<String, Object> getReport2(Long uid) throws Exception;
}
