package pers.fjl.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * 浏览量统计
 */
public class ViewsDTO {

    /**
     * 日期
     */
    private String day;

    /**
     * 访问量综合（每天）
     */
    private Integer viewsCount;

}
