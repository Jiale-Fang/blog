package pers.fjl.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * 请求参数的实体类
 * 请求日志需要记录的基本参数
 * @author fangjiale 2021年01月26日
 */
@Data
@AllArgsConstructor
@ToString
public class RequestLog {
    private String url;
    private String ip;
    private String classMethod;
    private Object[] args;
}
