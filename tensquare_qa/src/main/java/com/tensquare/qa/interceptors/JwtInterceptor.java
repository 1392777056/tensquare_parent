package com.tensquare.qa.interceptors;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auther 德哲
 * @date 2018/10/21 20:18.
 */
//@Configuration
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 放行， 当返回true的时候放行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     *
     * 通过往请求域中存入参数，来实现权限的甄别
     *        如果是普通用户存入：request.setAttribute("user_claims",claims)
     *        如果是管理员用户存入：request.setAttribute("admin_claims",claims)
     *        如果没有登录，就什么都不存
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取消息头
        String authHeader = request.getHeader("Authorization");
        // 判断authHeader是否合法
        if (!StringUtils.isEmpty(authHeader) && authHeader.startsWith("Bearer ")) {
            // 取出token
            String token = "";
            try {
                token = authHeader.split(" ")[1];
            } catch (Exception e) {
                throw new RuntimeException("消息头格式不合法");
            }
            // 验证token
            Claims claims = jwtUtil.parseJWT(token);
            if (claims != null) {
                if ("admin".equals(claims.get("roles"))) {
                    request.setAttribute("admin_claims",claims);
                }
                if ("user".equals(claims.get("roles"))) {
                    request.setAttribute("user_claims",claims);
                }
            }
        }
        return true;
    }
}
