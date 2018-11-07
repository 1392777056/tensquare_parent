package com.tensquare.manager;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @auther 德哲
 * @date 2018/11/7 20:53.
 */
@Component
public class ManagerFilter extends ZuulFilter {

    /**
     * 过滤器的执行时机
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器的执行优先级
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 过滤器是否执行
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器的核心方法
     * @return
     * @throws ZuulException
     *
     * 跨域操作都是二次请求，第一次是 域请求：此时不携带信息 ，它是请求方式是固定的 OPTIONS
     *                      第二次：正式的请求
     *                          它的请求方式是由具体的功能和RESTURL决定的，并且会携带数据，请求消息头和请求正文（请求体）
     */
    @Override
    public Object run() throws ZuulException {
        // 获取请求上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        // 获取消息头
        String authorization = request.getHeader("Authorization");

        // 跨域问题（二次请求） -- 处理
        String method = request.getMethod();
        if (method.equalsIgnoreCase("OPTIONS")){
            // 跨域的域请求---无需验证
            System.out.println("跨域的第一次域请求，放行");
            return null;
        }

        // 特殊操作，登录时候不需要获取消息头（因为登录时才会签发token）
        // 取出请求的URL
        String url = request.getRequestURL().toString();
        if (url.indexOf("/admin/login") > 0) {
            // 管理员登录无需验证消息头
            System.out.println("登录操作，过滤器直接放行了");
            return null;
        }

        // 判断消息头是否合法
        if (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer ")) {
            currentContext.addZuulRequestHeader("Authorization",authorization);
            return null;
        }
        // 消息头不合法。程序不能继续执行了
        currentContext.setSendZuulResponse(false);  //程序不在继续请求（不在请求微服务）  true 执行 false 不继续执行
        currentContext.setResponseStatusCode(401); // 响应状态码
        currentContext.setResponseBody("没有权限"); //响应正文
        currentContext.getResponse().setContentType("text/html;charset=UTF-8"); // 设置响应正文的类型和字符集
        // null代表执行
        return null;
    }
}
