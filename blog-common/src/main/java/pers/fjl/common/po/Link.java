package pers.fjl.common.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import pers.fjl.common.utils.JsonLongSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author fangjiale
 * @since 2021-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Link extends Model<Link> {

    private static final long serialVersionUID = 1L;

    /**
     * 友链id
     */
    @JsonSerialize(using = JsonLongSerializer.class)
    @TableId(value = "link_id")
    private Long linkId;

    /**
     * 友链名称
     */
    private String linkName;

    /**
     * 友链头像地址
     */
    private String avatarLink;

    /**
     * 友链地址
     */
    private String blogLink;

    /**
     * 友链博客描述
     */
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
     * 友链展示状态（1表示展示，0是不展示）
     */
    private boolean status;

    @Override
    protected Serializable pkVal() {
        return this.linkId;
    }

}
