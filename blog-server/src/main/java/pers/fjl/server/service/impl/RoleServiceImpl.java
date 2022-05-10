package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.fjl.common.constant.CommonConst;
import pers.fjl.common.dto.RoleDTO;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.User;
import pers.fjl.common.po.admin.Role;
import pers.fjl.common.po.admin.RoleMenu;
import pers.fjl.common.po.admin.RoleResource;
import pers.fjl.common.po.admin.UserRole;
import pers.fjl.common.vo.RoleVO;
import pers.fjl.common.vo.UserRoleVO;
import pers.fjl.server.dao.UserDao;
import pers.fjl.server.dao.admin.RoleDao;
import pers.fjl.server.dao.admin.TbRoleMenuDao;
import pers.fjl.server.dao.admin.TbRoleResourceDao;
import pers.fjl.server.dao.admin.TbUserRoleDao;
import pers.fjl.server.exception.BizException;
import pers.fjl.server.service.RoleMenuService;
import pers.fjl.server.service.RoleResourceService;
import pers.fjl.server.service.RoleService;
import pers.fjl.server.service.UserRoleService;
import pers.fjl.server.utils.RedisUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

    @Resource
    private RoleDao roleDao;
    @Resource
    private UserDao userDao;
    @Resource
    private TbUserRoleDao tbUserRoleDao;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private RoleResourceService roleResourceService;
    @Resource
    private RoleMenuService roleMenuService;

    @Override
    public Page<RoleDTO> listRoles(QueryPageBean queryPageBean) {
        Page<RoleDTO> roleDTOPage = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        roleDTOPage.setRecords(roleDao.listRoles(queryPageBean));
        Integer total = roleDao.selectCount(new LambdaQueryWrapper<Role>()
                .like(queryPageBean.getQueryString() != null, Role::getRoleName, queryPageBean.getQueryString()));
        roleDTOPage.setTotal(total);
        return roleDTOPage;
    }

    @Override
    public List<Role> listAllRoles() {
        return roleDao.selectList(new LambdaQueryWrapper<Role>()
                .select(Role::getRid, Role::getRoleName));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserRole(UserRoleVO userRoleVO) {
        // 更新用户角色和昵称
        User user = User.builder()
                .uid(userRoleVO.getUid())
                .nickname(userRoleVO.getNickname())
                .status(true)
                .build();
        userDao.updateById(user);

        //先删除用户拥有的角色
        tbUserRoleDao.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUid, userRoleVO.getUid()));
        List<UserRole> userRoleList = userRoleVO.getRoleIdList().stream()
                .map(roleId -> UserRole.builder()
                        .rid(roleId)
                        .uid(userRoleVO.getUid())
                        .build())
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "AdminMenus", condition = "#roleVO.getMenuIdList()!=null", allEntries = true),
                    @CacheEvict(value = {"ResourceList", "UserResource"}, condition = "#roleVO.getResourceIdList()!=null", allEntries = true)
            })
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateRole(RoleVO roleVO) {
        // 判断角色名是否存在
        Role roleDB = roleDao.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleName, roleVO.getRoleName()));
        if (Objects.nonNull(roleDB) && !roleDB.getRid().equals(roleVO.getRid())) {
            throw new BizException("该角色名已存在！");
        }
        // 保存或更新角色信息
        Role role = Role.builder()
                .rid(roleVO.getRid())
                .roleName(roleVO.getRoleName())
                .roleLabel(roleVO.getRoleLabel())
                .isDisable(CommonConst.FALSE)
                .build();
        this.saveOrUpdate(role);

        if (roleVO.getRid() != null) { // 代表是编辑role，因为id已经存在，先把存在的资源全删了
            roleResourceService.remove(new LambdaQueryWrapper<RoleResource>().eq(RoleResource::getRid, roleVO.getRid()));
            if (CollectionUtils.isNotEmpty(roleVO.getResourceIdList())) {    // 代表是保存或更新资源
                List<RoleResource> roleResourceList = roleVO.getResourceIdList().stream()
                        .map(resourceId -> RoleResource
                                .builder()
                                .rid(role.getRid())
                                .resourceId(resourceId)
                                .build())
                        .collect(Collectors.toList());
                roleResourceService.saveBatch(roleResourceList);
            }
        }
        // 如果是新增的角色，则rid为空，用户菜单不为空
        if (roleVO.getRid() != null && CollectionUtils.isNotEmpty(roleVO.getMenuIdList())) { // 同理
            roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRid, roleVO.getRid()));
        }
        if (CollectionUtils.isNotEmpty(roleVO.getMenuIdList())) {
            List<RoleMenu> roleMenuList = roleVO.getMenuIdList().stream().
                    map(menuId -> RoleMenu.builder()
                            .rid(role.getRid())
                            .menuId(menuId).build())
                    .collect(Collectors.toList());
            roleMenuService.saveBatch(roleMenuList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"ResourceList", "UserResource", "AdminMenus"}, allEntries = true)
    @Override
    public void deleteRoles(List<Integer> roleIdList) {
        roleDao.deleteBatchIds(roleIdList);
        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getRid, roleIdList));
        roleResourceService.remove(new LambdaQueryWrapper<RoleResource>().in(RoleResource::getRid, roleIdList));
    }
}
