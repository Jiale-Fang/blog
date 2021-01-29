package pers.fjl.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pers.fjl.server.interceptors.AuthenticationInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor())
//                .excludePathPatterns("/static/index.html")    //放行静态界面
                .addPathPatterns("/**");    //所有都要验证token
    }
}
