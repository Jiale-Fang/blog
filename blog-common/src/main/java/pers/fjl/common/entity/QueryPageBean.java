package pers.fjl.common.entity;

//import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
//import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 封装查询条件
 */
@Data
public class QueryPageBean implements Serializable {
    private Integer currentPage;    //页码
    private Integer pageSize;   //每页记录数
    private String queryString; //查询条件

    private Integer typeId; //分类id
    private Integer tagId; //标签id
    /**
     * 版权状态
     */
    private Integer copyright;
}
