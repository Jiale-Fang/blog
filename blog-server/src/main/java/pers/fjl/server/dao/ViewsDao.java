package pers.fjl.server.dao;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import pers.fjl.common.dto.ViewsDTO;
import pers.fjl.common.po.Views;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fangjiale
 * @since 2021-02-16
 */
@Repository
public interface ViewsDao extends BaseMapper<Views> {

    List<ViewsDTO> getViewsData(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
