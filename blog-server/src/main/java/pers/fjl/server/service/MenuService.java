package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.dto.LabelOptionDTO;
import pers.fjl.common.dto.MenuDTO;
import pers.fjl.common.dto.UserMenuDTO;
import pers.fjl.common.po.admin.Menu;

import java.util.List;

public interface MenuService extends IService<Menu> {

    /**
     * 获取后台管理菜单列表
     * @return list
     */
    List<MenuDTO> listMenus();

    /**
     * 获取当前用户的菜单列表
     * @return list
     */
    List<UserMenuDTO> listUserMenus();

    /**
     * 查看角色菜单选项
     *
     * @return 角色菜单选项
     */
    List<LabelOptionDTO> listMenuOptions();
}
