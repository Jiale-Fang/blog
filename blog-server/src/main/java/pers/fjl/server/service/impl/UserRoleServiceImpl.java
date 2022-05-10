package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pers.fjl.common.po.admin.UserRole;
import pers.fjl.server.dao.admin.TbUserRoleDao;
import pers.fjl.server.service.UserRoleService;


@Service
public class UserRoleServiceImpl extends ServiceImpl<TbUserRoleDao, UserRole> implements UserRoleService {

}
