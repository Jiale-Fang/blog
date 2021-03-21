package pers.fjl.crawler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import us.codecraft.webmagic.scheduler.RedisScheduler;

@SpringBootApplication
@EnableScheduling
@EnableEurekaClient
@MapperScan("pers.fjl.crawler.dao")
public class BlogCrawlerApplication {

//    @Value("${redis.host}")
//    private String redis_host;

    public static void main(String[] args) {
        SpringApplication.run(pers.fjl.crawler.BlogCrawlerApplication.class, args);
    }

    @Bean
    public RedisScheduler redisScheduler() {
        return new RedisScheduler("127.0.0.1");
    }
}
