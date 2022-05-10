package pers.fjl.common.vo;

import lombok.Data;
import pers.fjl.common.po.Blog;

import java.io.Serializable;
import java.util.List;

/**
 * 设计博客模块查询的附加条件
 */
@Data
public class ChatLogVO implements Serializable {

    private Long sender;
    private Long receiver;
    private Integer currentPage;


}
