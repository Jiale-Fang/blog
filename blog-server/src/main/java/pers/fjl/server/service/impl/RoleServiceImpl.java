package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pers.fjl.common.dto.RoleDTO;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.admin.Role;
import pers.fjl.server.dao.admin.RoleDao;
import pers.fjl.server.service.RoleService;

import javax.annotation.Resource;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

    @Resource
    private RoleDao roleDao;

    @Override
    public Page<RoleDTO> listRoles(QueryPageBean queryPageBean) {
        Page<RoleDTO> roleDTOPage = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        roleDTOPage.setRecords(roleDao.listRoles(queryPageBean));
        Integer total = roleDao.selectCount(new LambdaQueryWrapper<Role>()
                .like(queryPageBean.getQueryString() != null, Role::getRoleName, queryPageBean.getQueryString()));
        roleDTOPage.setTotal(total);
        return roleDTOPage;
    }
}
