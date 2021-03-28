package pers.fjl.common.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PageResult {

    private List<Map<String, Object>> records;

    private Integer total;
}
