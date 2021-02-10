package pers.fjl.extension.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.cache.annotation.Cacheable;
import pers.fjl.extension.po.Link;
import pers.fjl.extension.dao.LinkDao;
import pers.fjl.extension.service.LinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
        wrapper.eq("data_status", 1);
        return linkDao.selectList(wrapper);
    }

    @Override
    public boolean addLink(Link link) {
        int i = linkDao.insert(link);
        if (i != 1) {
            throw new RuntimeException("添加友链失败！");
        }
        return true;
    }
}
