package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pers.fjl.common.dto.LabelOptionDTO;
import pers.fjl.common.dto.ResourceDTO;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.admin.Resource;
import pers.fjl.server.dao.admin.ResourceDao;
import pers.fjl.server.service.ResourceService;
import pers.fjl.server.utils.BeanCopyUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 资源服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-14
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceDao, Resource> implements ResourceService {
    @javax.annotation.Resource
    private ResourceDao resourceDao;

    @Cacheable(value = {"ResourceList"}, key = "#root.methodName")
    public List<String> getResourceList() {
        QueryWrapper<Resource> tbResourceQueryWrapper = new QueryWrapper<>();
        tbResourceQueryWrapper.gt("id", 275);
        List<Resource> tbResourceList = resourceDao.selectList(null);
        List<String> resourceList = tbResourceList.stream().map((Resource r) -> r.getRequestMethod() + r.getUrl()).collect(Collectors.toList());
        return resourceList;
    }

    @Cacheable(value = {"UserResource"}, key = "#uid")
    public List<String> getUserResource(Long uid) {
        return resourceDao.getUserResource(uid);
    }

    @Override
    public List<LabelOptionDTO> listResourceOptions() {
        // 查询资源列表
        List<Resource> resourceList = resourceDao.selectList(new LambdaQueryWrapper<Resource>()
                .select(Resource::getId, Resource::getResourceName, Resource::getParentId)
                .eq(Resource::getIsAnonymous, 0));
        // 获取所有模块
        List<Resource> parentList = listResourceModule(resourceList);
        // 根据父id分组获取模块下的资源
        Map<Integer, List<Resource>> childrenMap = listResourceChildren(resourceList);
        // 组装父子数据
        return parentList.stream().map(item -> {
            List<LabelOptionDTO> list = new ArrayList<>();
            List<Resource> children = childrenMap.get(item.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                list = children.stream()
                        .map(resource -> LabelOptionDTO.builder()
                                .id(resource.getId())
                                .label(resource.getResourceName())
                                .build())
                        .collect(Collectors.toList());
            }
            return LabelOptionDTO.builder()
                    .id(item.getId())
                    .label(item.getResourceName())
                    .children(list)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<ResourceDTO> listResources(QueryPageBean queryPageBean) {
        List<Resource> resourceList = resourceDao.selectList(new LambdaQueryWrapper<Resource>()
                .like(StringUtils.isNotBlank(queryPageBean.getQueryString()), Resource::getResourceName, queryPageBean.getQueryString()));
        List<Resource> parentList = listResourceModule(resourceList);
        Map<Integer, List<Resource>> childrenMap = listResourceChildren(resourceList);
        //将子接口加入到父模块的children列表中
        List<ResourceDTO> resourceDTOList = parentList.stream().map(item -> {
            ResourceDTO resourceDTO = BeanCopyUtils.copyObject(item, ResourceDTO.class);
            List<ResourceDTO> childrenList = BeanCopyUtils.copyList(childrenMap.get(item.getId()), ResourceDTO.class);
            resourceDTO.setChildren(childrenList);
            childrenMap.remove(item.getId());
            return resourceDTO;
        }).collect(Collectors.toList());
        // 拼接未取出的资源
        if (CollectionUtils.isNotEmpty(childrenMap)) {
            List<Resource> childrenList = new ArrayList<>();
            childrenMap.values().forEach(childrenList::addAll);
            List<ResourceDTO> childrenDTOList = childrenList.stream()
                    .map(item -> BeanCopyUtils.copyObject(item, ResourceDTO.class))
                    .collect(Collectors.toList());
            resourceDTOList.addAll(childrenDTOList);
        }
        return resourceDTOList;
    }

    private List<Resource> listResourceModule(List<Resource> resourceList) {
        return resourceList.stream()
                .filter(item -> Objects.isNull(item.getParentId()))
                .collect(Collectors.toList());
    }

    private Map<Integer, List<Resource>> listResourceChildren(List<Resource> resourceList) {
        return resourceList.stream().filter(item -> Objects.nonNull(item.getParentId()))
                .collect(Collectors.groupingBy(Resource::getParentId));
    }
}
