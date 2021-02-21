package pers.fjl.common.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import pers.fjl.common.utils.JsonLongSerializer;

import java.time.LocalDateTime;

/**
 * <p>
 * 点赞量实体类
 * </p>
 *
 * @author fangjiale
 * @since 2021-02-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ThumbsUp extends Model<ThumbsUp> {

    private static final long serialVersionUID = 1L;

    /**
     * 博客id
     */
    @JsonSerialize(using = JsonLongSerializer.class)
    private Long blogId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("`create_time`")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    /**
     * 用户id
     */
    @JsonSerialize(using = JsonLongSerializer.class)
    private Long uid;

}
