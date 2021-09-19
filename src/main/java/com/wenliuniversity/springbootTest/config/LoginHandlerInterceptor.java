package com.wenliuniversity.springbootTest.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**这个拦截器处理类不需要用@Configuration来标识这是一个配置类，因为我们在MyMVCConfig里会以Bean的形式注入容器
 * @author HuangHai
 * @date 2021/4/11 22:54
 */

public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object status = request.getSession().getAttribute("status");
        if (status==null){//未登录
            request.setAttribute("msg","没有权限，请先登录！");
            System.out.println("拦截器启动");
            request.getRequestDispatcher("/index").forward(request,response);
            return false;
        }else {//已经登录
            return true;
        }
    }
}
