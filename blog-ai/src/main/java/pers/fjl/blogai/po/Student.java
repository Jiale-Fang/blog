package pers.fjl.blogai.po;

import lombok.Data;

/**
 * 用于随机森林的实体类
 */
@Data
public class Student {//特征是离散值，所以实体类的基础类别都是整形
    private int math;
    private int eng;
    private int chi;
    private int h1;
    private int h2;
    private int h3;
    private int point;
}
