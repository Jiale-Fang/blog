package pers.fjl.common.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    @TableId(value = "tag_id")
    private Long tagId;

    private String tagName;


    @Override
    protected Serializable pkVal() {
        return this.tagId;
    }

}
