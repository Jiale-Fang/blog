package pers.fjl.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})   // 该模块不用查数据库
//@EnableElasticsearchRepositories(basePackages = "pers.fjl.search.dao")
@EnableEurekaClient
//@SpringBootApplication
public class SearchApplication {

    public static void main(String[] args) {
//
//        /**
//         * Springboot整合Elasticsearch 在项目启动前设置一下的属性，防止报错
//         * 解决netty冲突后初始化client时还会抛出异常
//         * availableProcessors is already set to [12], rejecting [12]
//         ***/
//        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(SearchApplication.class, args);
    }

}
