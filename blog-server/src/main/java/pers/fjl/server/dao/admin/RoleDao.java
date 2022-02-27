package pers.fjl.server.dao.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import pers.fjl.common.dto.RoleDTO;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.admin.Role;

import java.util.List;

/**
 * <p>
 * 菜单Mapper 接口
 * </p>
 *
 * @author fangjiale
 * @since 2022-01-14
 */
@Repository
public interface RoleDao extends BaseMapper<Role> {

    /**
     * 获取角色列表和当前角色拥有的resource和menu
     * @param queryPageBean 分页实体
     * @return list
     */
    List<RoleDTO> listRoles(@Param("queryPageBean") QueryPageBean queryPageBean);

    /**
     * 根据用户id获取角色列表
     * @param uid 用户id
     * @return 角色标签
     */
    List<String> listRolesByUid(@Param(value = "uid") Long uid);
}
