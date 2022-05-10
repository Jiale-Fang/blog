package pers.fjl.encrypt.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import pers.fjl.common.constant.CacheKey;
import pers.fjl.encrypt.api.ResourceApi;
import pers.fjl.encrypt.utils.JwtUserInfo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import pers.fjl.encrypt.utils.RedisUtil;

import java.util.Objects;

import static pers.fjl.common.constant.RedisConst.TOKEN_ALLOW_LIST;
import static pers.fjl.common.constant.RedisConst.USER_RESOURCE_KEY;
import static pers.fjl.common.enums.StatusCodeEnum.*;
import static pers.fjl.common.utils.JWTUtils.getTokenInfo;

@Component
@Slf4j
@CrossOrigin
public class AccessFilter extends BaseFilter {
    @Resource
    private ResourceApi resourceApi;
    @Resource
    private RedisUtil redisUtil;

    private final String tokenHeader = "Authorization";

    @Override
    public String filterType() {
        //过滤器在什么环境下执行，解密操作需要在转发之前执行
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        //设置过滤器的执行顺序，数值越大顺序越后
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 10;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 要放行不需要token的uri请求，需要token的请求要进行jwt解码，获取该用户的权限
     * 第1步：判断当前请求uri是否需要忽略
     * 第2步：获取当前请求的请求方式和uri，拼接成GET/user/page这种形式，称为权限标识符
     * 获取token的信息，看token是否已经失效
     * 第3步：从缓存中获取所有需要进行鉴权的资源(同样是由资源表的method字段值+url字段值拼接成)，如果没有获取到则通过Feign调用权限服务获取并放入缓存中
     * 第4步：判断这些资源是否包含当前请求的权限标识符，如果不包含当前请求的权限标识符，则返回未经授权错误提示
     * 第5步：如果包含当前的权限标识符，则从zuul header中取出用户id，用token根据用户id取出缓存中的用户拥有的权限，如果没有取到则通过Feign调用权限服务获取并放入缓存，判断用户拥有的权限是否包含当前请求的权限标识符
     * 第6步：如果用户拥有的权限包含当前请求的权限标识符则说明当前用户拥有权限，直接放行
     * 第7步：如果用户拥有的权限不包含当前请求的权限标识符则说明当前用户没有权限，返回未经授权错误提示
     */
    public Object run() throws ZuulException {
        //1:判断当前请求uri是否需要忽略
        if (isIgnoreToken()) {
            //放行
            return null;
        }

        //2:获取当前请求的请求方式和uri，拼接成GET/user/getUserList这种形式，作为权限标识符
        RequestContext ctx = RequestContext.getCurrentContext(); //获取容器
        HttpServletRequest request = ctx.getRequest();
        String uri = request.getRequestURI();
        String method = request.getMethod();    //GET\POST...

        JwtUserInfo userInfo = null;
        try {
            //3:获取token的信息，看token是否已经失效
            String token = request.getHeader(this.tokenHeader);
            DecodedJWT verify = getTokenInfo(token);
            Long uid = Long.parseLong(verify.getClaim("id").asString());
            if (Objects.isNull(redisUtil.get(TOKEN_ALLOW_LIST + uid))) {
                errorResponse(TOKEN_EXPIRED.getDesc(), TOKEN_EXPIRED.getCode(), 200);
            }

            // /api/....   截取第二个/以即其之后的字符串
            // /api/server/....   截取第三个/以即其之后的字符串
            String result = uri.substring(uri.indexOf("/server") + 7);

            String permission = method + result;    //自己定义的鉴权标识符
            log.info("请求路径:{}", permission);

            //4.从缓存中取资源表模板
            List<String> list = (List<String>) redisUtil.get(CacheKey.RESOURCE);
            if (list == null) {
                //缓存中没有资源表，已经做了cacheable缓存
                list = resourceApi.getResourceList();
            }

            //5.判断当前请求（即method+uri，是否合法，要与资源表比对）
            long count = list.stream().filter((resource) -> {
                return permission.startsWith(resource);
            }).count();

            if (count == 0) {
                //当前请求是不合法的，资源表中没有，直接返回未授权异常
                errorResponse(UNAUTHORIZED.getDesc(), UNAUTHORIZED.getCode(), 200);
            }

            //6.根据token，取出当前用户拥有的权限资源表，判断当前用户是否有权限访问当前请求
            List<String> userResource = (List<String>) redisUtil.get(USER_RESOURCE_KEY + String.valueOf(uid));
            if (userResource == null) {
                //缓存中不存在
                userResource = resourceApi.getUserResource(String.valueOf(uid));    // 已经做了cacheable缓存
            }

            //7.判断用户拥有的权限是否包含当前请求的权限标识符，有则代表用户有权限，直接放行
            count = userResource.stream().filter((resource) -> {
                return permission.startsWith(resource);
            }).count();

            if (count > 0) {
                log.info("测试count:{}", permission + "  ");
                //网关从前端接收过来的request后，还要再加上token到头转发request，否则后端服务器会拦截
                ctx.addZuulRequestHeader("Authorization", token);
                //需要设置request请求头中的Content-Type为json格式，否则api接口模块就需要进行url转码操作
                ctx.addZuulRequestHeader("Content-Type", String.valueOf(MediaType.APPLICATION_JSON) + ";charset=UTF-8");
                return null;
            } else {
                errorResponse(PERMISSION_DENIED.getDesc(), PERMISSION_DENIED.getCode(), 200);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorResponse(TOKEN_EXPIRED.getDesc(), TOKEN_EXPIRED.getCode(), 200);
            return null;
        }
    }
}
