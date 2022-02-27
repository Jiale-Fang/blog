package pers.fjl.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerAutoConfig {
    @Bean
    public Docket creatRestApi(){
        ApiInfo apiInfo = new ApiInfoBuilder().title("接口文档").contact(new Contact("tcefrep","https://www.tcefrep.site/","1626680964@qq.com")).version("1.0").description("接口文档").build();
        return new Docket(DocumentationType.SWAGGER_2).
                apiInfo(apiInfo).groupName("服务端接口").
                select().apis(RequestHandlerSelectors.
                basePackage("pers.fjl.server.controller")).build();
    }
}
