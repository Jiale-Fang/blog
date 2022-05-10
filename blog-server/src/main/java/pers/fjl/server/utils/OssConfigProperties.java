package pers.fjl.server.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "upload.oss.file")
public class OssConfigProperties {

    private String url;

    private String endpoint;

    private String keyId;

    private String keySecret;

    private String bucketName;

}
