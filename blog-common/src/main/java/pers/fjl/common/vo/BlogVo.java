package pers.fjl.common.vo;

import lombok.Data;
import pers.fjl.common.po.Blog;

import java.io.Serializable;

/**
 * 设计博客模块查询的附加条件
 */
@Data
public class BlogVo extends Blog implements Serializable {

    private String typeName;    // 分类名称
    private String nickname;    //用户昵称
    private String avatar;      //用户头像

}
