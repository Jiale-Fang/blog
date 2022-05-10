package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.dto.LabelOptionDTO;
import pers.fjl.common.dto.ResourceDTO;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.admin.Resource;
import pers.fjl.common.vo.ResourceVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fangjiale
 * @since 2022-01-14
 */
public interface ResourceService extends IService<Resource> {

    /**
     * 资源列表
     * @return 资源list
     */
    List<String> getResourceList();

    /**
     * 查询用户的权限
     * @return 资源list
     */
    List<String> getUserResource(Long uid);

    /**
     * 查看角色菜单选项
     *
     * @return 角色菜单选项
     */
    List<LabelOptionDTO> listResourceOptions();

    /**
     * 查看资源列表
     *
     * @param queryPageBean 查询条件
     * @return 资源列表
     */
    List<ResourceDTO> listResources(QueryPageBean queryPageBean);

    /**
     * 新增或修改资源
     * @param resourceVO 资源
     */
    void saveOrUpdateResource(ResourceVO resourceVO);

    void deleteResource(Integer resourceId);
}

