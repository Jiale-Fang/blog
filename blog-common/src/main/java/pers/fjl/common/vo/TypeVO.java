package pers.fjl.common.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.fjl.common.po.Type;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class TypeVO extends Type implements Serializable {
    private Integer typeCount;
}
