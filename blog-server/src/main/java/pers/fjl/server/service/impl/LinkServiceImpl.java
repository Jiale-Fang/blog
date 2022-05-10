package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pers.fjl.common.constant.CommonConst;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Link;
import pers.fjl.server.annotation.OptLog;
import pers.fjl.server.dao.LinkDao;
import pers.fjl.server.service.LinkService;

import javax.annotation.Resource;
import java.util.List;

import static pers.fjl.common.constant.OptTypeConst.REMOVE;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-02-08
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkDao, Link> implements LinkService {
    @Resource
    private LinkDao linkDao;

    @Cacheable(value = {"LinkMap"})
    public List<Link> getLinkList() {
        QueryWrapper<Link> wrapper = new QueryWrapper<>();
        wrapper.eq("status", CommonConst.SHOW_LINKS);
        return linkDao.selectList(wrapper);
    }

    @CacheEvict(value = {"linkPage"}, allEntries = true)
    public boolean addLink(Link link) {
        int i = linkDao.insert(link);
        return i == 1;
    }

    @Cacheable(value = {"linkPage"}, condition = "#queryPageBean.getQueryString()==null", key = "#root.methodName+'['+#queryPageBean.currentPage+']'")
    public Page<Link> listLink(QueryPageBean queryPageBean) {
        Page<Link> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<Link> linkQueryWrapper = new QueryWrapper<>();
        linkQueryWrapper.like(queryPageBean.getQueryString() != null, "link_name", queryPageBean.getQueryString());
        return linkDao.selectPage(page, linkQueryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateLinkDisable(Link link) {
        linkDao.updateById(link);
    }

    @CacheEvict(value = {"linkPage"}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateFriendLink(Link link) {
        this.saveOrUpdate(link);
    }
}
