package pers.fjl.crawler.po;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 从CSDN上爬取下来的博客实体类
 * </p>
 *
 * @author fangjiale
 * @since 2021-03-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CrawledBlog extends Model<CrawledBlog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "blog_id")
    private Long blogId;

    private String title;

    private String nickname;

    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    private String firstPicture;

    private Integer thumbs;

    private Integer views;

    private String description;

    private String avatar;

    @Override
    protected Serializable pkVal() {
        return this.blogId;
    }

}
