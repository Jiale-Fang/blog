package pers.fjl.server.search.index;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "blog-search", type = "type1", createIndex = true)
public class BlogInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 博客id
     */
    @Id
    private Long blogId;

    /**
     * 标题
     * 不用分词（Keyword）
     */
    @Field(type = FieldType.Text, index = true, analyzer = "ik_max_word", searchAnalyzer = "ik_smart", store = true)
    private String title;

    /**
     * 内容
     * 是否索引：看该域是否能被搜索
     * 是否分词：看搜索时是整体匹配还是单词匹配
     * 是否存储，就是在页面上是否显示
     */
    @Field(type = FieldType.Text, index = true, analyzer = "ik_max_word", searchAnalyzer = "ik_smart", store = true)
    private String content;

    /**
     * 博客摘要
     */
    @Field(type = FieldType.Text, index = true, analyzer = "ik_max_word", searchAnalyzer = "ik_smart", store = true)
    private String description;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    /**
     * 首图
     */
    private String firstPicture;

    /**
     * 点赞数
     */
    private Integer thumbs;

    /**
     * 浏览次数
     */
    private Integer views;

    private String typeName;    // 分类名称

    private String nickname;    //用户昵称

    private String avatar;      //用户头像

}
