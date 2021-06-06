package pers.fjl.server.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss.file")
public class ConstantPropertiesUtils{

    private String endpoint;

    private String keyid;

    private String keysecret;

    private String bucketname;

}
