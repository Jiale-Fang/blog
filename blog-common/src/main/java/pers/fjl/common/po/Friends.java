package pers.fjl.common.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
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
import org.springframework.format.annotation.DateTimeFormat;
import pers.fjl.common.utils.JsonLongSerializer;

/**
 * <p>
 *
 * </p>
 *
 * @author fangjiale
 * @since 2021-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Friends extends Model<Friends> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户本人的id
     */
    @JsonSerialize(using = JsonLongSerializer.class)
    private Long uid;

    /**
     * 拥有的朋友的id
     */
    @JsonSerialize(using = JsonLongSerializer.class)
    private Long friendId;

    /**
     * 添加好友的时间
     */
    @TableField("`create_time`")
    @DateTimeFormat(pattern = "MM-dd HH:mm")
    @JsonFormat(pattern = "MM-dd HH:mm", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.uid;
    }

}
