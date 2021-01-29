package pers.fjl.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * <p>
 *  SpringSecurity配置
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-27
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {  //将SpringSecurity的拦截请求全部放行
        http.headers().frameOptions().disable();    //Spring Security4默认是将'X-Frame-Options' 设置为 'DENY'
        http.authorizeRequests()
                .antMatchers("/**")
                .permitAll()
//                .anyRequest().authenticated()
                .and()
                .csrf().disable();
//        http.formLogin();
    }
}
