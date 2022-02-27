package pers.fjl.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 文章排行
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogRankDTO {

    /**
     * 标题
     */
    private String title;

    /**
     * 浏览量
     */
    private Integer views;


}
