package pers.fjl.server.dao.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import pers.fjl.common.po.admin.RoleMenu;
import pers.fjl.common.po.admin.RoleResource;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fjl
 * @since 2022-01-15
 */
@Repository
public interface TbRoleMenuDao extends BaseMapper<RoleMenu> {

}
