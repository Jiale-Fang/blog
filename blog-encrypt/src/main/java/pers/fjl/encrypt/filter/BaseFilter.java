package pers.fjl.encrypt.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import pers.fjl.encrypt.adapter.IgnoreTokenConfig;
import javax.servlet.http.HttpServletRequest;

/**
 * 基础过滤器(还没测试。。
 */
@Slf4j
public abstract class BaseFilter extends ZuulFilter {

    //判断当前请求uri是否需要放行
    protected boolean isIgnoreToken() {
        //动态获取当前请求的uri
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String uri = request.getRequestURI();
        // /api/server/....   截取第三个/以即其之后的字符串
        String result = uri.substring(uri.indexOf("/server")+7);
        log.info("是否放行该请求:{}",IgnoreTokenConfig.isIgnoreToken(result));
        return IgnoreTokenConfig.isIgnoreToken(result);
    }

    //用户没有权限，网关抛异常
    protected void errorResponse(String errMsg, int errCode, int httpStatusCode) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(httpStatusCode);
        ctx.addZuulResponseHeader(
                "Content-Type", "application/json;charset=UTF-8");
        if (ctx.getResponseBody() == null) {
            ctx.setResponseBody("该用户没有权限，出现异常");
            //不进行路由，直接返回
            ctx.setSendZuulResponse(false);
        }
    }

}
