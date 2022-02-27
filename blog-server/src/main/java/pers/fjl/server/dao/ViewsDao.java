package pers.fjl.server.dao;

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

    @Select("SELECT DATE_FORMAT( create_time, \"%Y-%m-%d\" ) as days, COUNT(DATE_FORMAT( create_time, \"%Y-%m-%d\" )) as viewsCount \n" +
            "FROM `views` " +
            "WHERE " +
            "create_time >= #{startTime} " +
            "AND create_time <=  #{endTime} " +
            "group by days\n")
    List<ViewsDTO> getViewsData(Date startTime, Date endTime);
}
