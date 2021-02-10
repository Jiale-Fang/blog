package pers.fjl.extension.dao;

import org.springframework.stereotype.Repository;
import pers.fjl.extension.po.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fangjiale
 * @since 2021-02-08
 */
@Repository
public interface MessageDao extends BaseMapper<Message> {

}
