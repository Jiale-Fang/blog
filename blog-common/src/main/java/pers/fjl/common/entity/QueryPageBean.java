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

    private Long typeId; //分类id
    private Long tagId; //标签id
}
