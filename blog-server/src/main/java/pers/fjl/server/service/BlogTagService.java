package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.po.BlogTag;

/**
 * <p>
 *  标签博客中间表服务类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
public interface BlogTagService extends IService<BlogTag> {

    boolean addOneBlogTag(Long blogId, Long[] value);
}
