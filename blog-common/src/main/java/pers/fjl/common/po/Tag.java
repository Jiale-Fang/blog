package pers.fjl.common.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.fjl.common.utils.JsonLongSerializer;

/**
 * <p>
 *
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Tag extends Model<Tag> {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = JsonLongSerializer.class )
    @TableId(value = "tag_id")
    private Long tagId;

    private String tagName;


    @Override
    protected Serializable pkVal() {
        return this.tagId;
    }

}
