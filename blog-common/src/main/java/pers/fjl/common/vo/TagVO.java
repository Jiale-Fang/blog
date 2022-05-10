package pers.fjl.common.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.fjl.common.po.Tag;
import pers.fjl.common.po.Type;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class TagVO extends Tag implements Serializable {
    private Integer tagCount;
}
