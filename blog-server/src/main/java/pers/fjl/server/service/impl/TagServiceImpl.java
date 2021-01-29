package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pers.fjl.common.po.Tag;
import pers.fjl.server.dao.TagDao;
import pers.fjl.server.service.TagService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagDao, Tag> implements TagService {
    @Resource
    private TagDao tagDao;

    @Override
    public List<Tag> getTagList() {
        return tagDao.selectList(null);
    }
}
