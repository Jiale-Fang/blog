package pers.fjl.server.dao.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import pers.fjl.common.po.admin.OperationLog;


/**
 * 操作日志
 *
 */
@Repository
public interface OperationLogDao extends BaseMapper<OperationLog> {
}
