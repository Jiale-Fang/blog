package pers.fjl.server.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static com.sun.el.parser.ELParserConstants.FALSE;

/**
 * 用户信息
 *
 */
@Data
@Builder
public class UserDetailDTO implements UserDetails {

    /**
     * 用户账号id
     */
    private Long uid;

    /**
     * 邮箱号
     */
    private String email;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户角色
     */
    private List<String> roleList;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 点赞文章集合
     */
    private Set<Object> articleLikeSet;

    /**
     * 点赞评论集合
     */
    private Set<Object> commentLikeSet;

    /**
     * 用户登录ip
     */
    private String lastIp;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 是否禁用
     */
    private boolean status;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 最近登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 登录类型
     */
    private Integer loginType;

    /**
     * 操作系统
     */
    private String os;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roleList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * 账号是否过期
     * @return boolean
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status;
    }

    /**
     * 认证是否过期
     * @return boolean
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * 重写比较方法,SpringSecurity根据用户名来比较是否同一个用户
     */
    @Override
    public boolean equals(Object o){
        return o.toString().equals(this.username);
    }

    @Override
    public int hashCode(){
        return username.hashCode();
    }


    @Override
    public String toString() {
        return this.username;
    }

}
