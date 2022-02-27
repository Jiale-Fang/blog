package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.dto.OperationLogDTO;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.admin.OperationLog;

/**
 * 操作日志服务
 *
 */
public interface OperationLogService extends IService<OperationLog> {

    /**
     * 查询日志列表
     *
     * @param queryPageBean 条件
     * @return 日志列表
     */
    Page<OperationLogDTO> listOperationLogs(QueryPageBean queryPageBean);

}
