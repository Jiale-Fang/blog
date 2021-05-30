package pers.fjl.crawler.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TextClassifyVo {
    private String type;
    private double result;
}
