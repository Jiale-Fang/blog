package pers.fjl.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Tag;
import pers.fjl.common.vo.TagVO;

import java.util.List;

/**
 * <p>
 * 标签服务类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
public interface TagService extends IService<Tag> {

    /**
     * 获取后台管理的tag标签列表
     *
     * @return list
     */
    List<Tag> getTagList();

    /**
     * 获取每个分类的博客数量
     *
     * @return list
     */
    List<TagVO> getTagCount();

    /**
     * 获取个人后台管理标签列表
     * @param queryPageBean
     * @return page
     */
    Page<Tag> findPage(QueryPageBean queryPageBean);

    /**
     * 获取管理员后台管理标签列表
     * @param queryPageBean 分页实体
     * @return page page数据
     */
    Page<TagVO> adminTag(QueryPageBean queryPageBean);

    /**
     * 搜索tag
     * @param queryPageBean
     * @return
     */
    List<Tag> searchTags(QueryPageBean queryPageBean);

    /**
     * 添加或更改标签
     * @param tag 标签
     * @return
     */
    boolean saveOrUpdateType(Tag tag);

    /**
     * 删除标签
     * @param tagIdList 要删除的标签id列表
     */
    void delete(List<Integer> tagIdList);
}
