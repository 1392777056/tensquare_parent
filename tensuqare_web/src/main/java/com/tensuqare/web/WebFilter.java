package com.tensuqare.web;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 网关过滤器，用于解决请求消息头的问题
 * @auther 德哲
 * @date 2018/11/7 20:41.
 */
@Component
public class WebFilter extends ZuulFilter {

    /**
     * 确定过滤器的执行时机
     * 取值4个
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器的执行时机，数值越大，优先级越低
     * 类似于 load-on-startup
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 过滤器是否执行  true false
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器的核心功能，我们需要实现的功能都写在此处
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        // 1.获取Request的上下文对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 取出请求域中的消息头和值
        HttpServletRequest request = requestContext.getRequest();
        String authorization = request.getHeader("Authorization");
        // 2.通过添加的方式，提供给指定的微服务
        if (!StringUtils.isEmpty(authorization)) {
            // 5.通过添加的方式，提供给指定的微服务
            requestContext.addZuulRequestHeader("Authorization",authorization);
        }
        // 3.返回null表示放行
        return null;
    }
}
