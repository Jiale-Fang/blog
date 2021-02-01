package pers.fjl.common.vo;

import lombok.Data;
import pers.fjl.common.po.Tag;
import pers.fjl.common.po.Type;

import java.io.Serializable;

@Data
public class TagVo extends Tag implements Serializable {
    private Integer tagCount;
}
