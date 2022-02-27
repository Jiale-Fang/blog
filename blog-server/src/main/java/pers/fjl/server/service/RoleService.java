package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.dto.RoleDTO;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.admin.Role;

public interface RoleService extends IService<Role> {

    /**
     * 获取后台角色数据
     * @param queryPageBean 分页实体
     * @return list
     */
    Page<RoleDTO> listRoles(QueryPageBean queryPageBean);
}
