package pers.fjl.common.vo;

import lombok.Data;
import pers.fjl.common.po.Blog;

@Data
public class AddBlogVo extends Blog {
    private Long[] value; // 存放的是博客对应的标签列表
}
