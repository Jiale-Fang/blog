package pers.fjl.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.fjl.common.po.Tag;
import pers.fjl.common.vo.TypeVO;

import java.util.List;


/**
 * 博客后台信息
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogBackInfoDTO {
    /**
     * 访问量
     */
    private Integer viewsCount;

    /**
     * 留言量
     */
    private Integer messageCount;

    /**
     * 用户量
     */
    private Integer userCount;

    /**
     * 文章量
     */
    private Integer articleCount;

    /**
     * 分类统计
     */
    private List<TypeVO> typeList;

    /**
     * 标签列表
     */
    private List<Tag> tagList;

    /**
     * 文章统计列表
     */
    private List<BlogStatisticsDTO> articleStatisticsList;

    /**
     * 一周用户量集合
     */
    private List<ViewsDTO> viewsDTOList;

    /**
     * 文章浏览量排行
     */
    private List<BlogRankDTO> blogRankDTOList;

}
