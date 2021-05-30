package pers.fjl.extension;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
//@EnableEurekaClient
@MapperScan("pers.fjl.extension.dao")
public class BlogExtensionApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogExtensionApplication.class, args);
    }

}
