package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pers.fjl.common.po.Blog;
import pers.fjl.common.po.Views;
import pers.fjl.server.dao.ViewsDao;
import pers.fjl.server.service.ViewsService;

import javax.annotation.Resource;

/**
 * <p>
 * 访问量服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-02-18
 */
@Service
public class ViewsServiceImpl extends ServiceImpl<ViewsDao, Views> implements ViewsService {
    @Resource
    private ViewsDao viewsDao;

    @Override
    public boolean addViews(Long blogId, String host) { // 访问量暂且较少，多了再考虑短时间内访问量只增加一次的问题
        Views views = new Views();
        views.setIp(host);
        views.setBlogId(blogId);
        viewsDao.insert(views);
        return true;
    }
}
