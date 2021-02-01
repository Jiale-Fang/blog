package pers.fjl.common.vo;

import lombok.Data;
import pers.fjl.common.po.Type;

import java.io.Serializable;

@Data
public class TypeVo extends Type implements Serializable {
    private Integer typeCount;
}
