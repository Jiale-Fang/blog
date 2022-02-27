package pers.fjl.server.dao.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import pers.fjl.common.po.admin.Resource;

import java.util.List;

/**
 * <p>
 * 资源Mapper 接口
 * </p>
 *
 * @author fangjiale
 * @since 2022-01-14
 */
@Repository
public interface ResourceDao extends BaseMapper<Resource> {

    /**
     * 获取用户拥有的权限数据
     * @param uid 用户id
     * @return list
     */
    @Select("select concat(r.request_method,r.url) result " +
            "from tb_resource r, tb_user_role ur, tb_role_resource rr " +
            "where ur.uid = #{uid} AND rr.rid = ur.rid AND rr.resource_id = r.id")
    List<String> getUserResource(Long uid);
}
