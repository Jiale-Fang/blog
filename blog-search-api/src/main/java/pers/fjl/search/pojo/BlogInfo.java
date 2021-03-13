package pers.fjl.search.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;
import pers.fjl.common.utils.JsonLongSerializer;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "blog-search", type = "type1")
public class BlogInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 博客id
     */
    @Id
    private String blogId;

    /**
     * 标题
     * 不用分词（Keyword）
     */
    @Field(index = true, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", store = true)
    private String title;

//    /**
//     * 内容
//     * 是否索引：看该域是否能被搜索
//     * 是否分词：看搜索时是整体匹配还是单词匹配
//     * 是否存储，就是在页面上是否显示
//     */
//    @Field(type = FieldType.Text, analyzer = "ik_max_word", index = true, store = false, searchAnalyzer = "ik_max_word")
//    private String content;
//
////    /**
////     * 博客摘要
////     */
////    @Field(analyzer = "ik_max_word", index = true, store = true, searchAnalyzer = "ik_max_word")
////    private String description;
//
//    /**
//     * 创建时间
//     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    private LocalDateTime createTime;
//
//    /**
//     * 首图
//     */
//    private String firstPicture;
//
//    /**
//     * 点赞数
//     */
//    private Integer thumbs;
//
//    /**
//     * 浏览次数
//     */
//    private Integer views;
//
//    private String typeName;    // 分类名称
//
//    private String nickname;    //用户昵称
//
//    private String avatar;      //用户头像

}
