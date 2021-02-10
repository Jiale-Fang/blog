package pers.fjl.extension;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableCaching
@EnableEurekaClient
@MapperScan("pers.fjl.extension.dao")
public class BlogExtensionApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogExtensionApplication.class, args);
    }

}
