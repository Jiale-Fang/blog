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
public class BlogTag extends Model<BlogTag> {

    private static final long serialVersionUID = 1L;

    private Long blogId;

    private Long tagId;


    @Override
    protected Serializable pkVal() {
        return this.blogId;
    }

}
