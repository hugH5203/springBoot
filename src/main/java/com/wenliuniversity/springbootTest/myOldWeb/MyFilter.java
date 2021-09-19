package com.wenliuniversity.springbootTest.myOldWeb;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**  过滤器
 * 由于springboot是以jar包的形式启动它内置的servlet容器，然后由此来启动springboot的web应用，所以他是没有web.xml与三大组件的，
 * 但是我们可以自己注册回来，springboot默认有相关的自动配置类，我们只需要注册相关的bean到spring容器即可
 * @author HuangHai
 * @date 2021/4/15 16:36
 */
@WebFilter("/*")//（注解注册可用，即在入口类上写@ServletComponentScan就可以不需代码注册）由于老式的filter在这里是代码注册的，所以映射这个过滤器的虚拟路径也必须到注册Bean的哪个方法与类里去配置
public class MyFilter implements javax.servlet.Filter {


    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("老式web的过滤器，比springboot或springMVC的拦截器厉害");
        System.out.println("过滤器什么资源都拦截（包括静态），而拦截器只拦截controller声明的路径");
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

    public void destroy() {
    }

}
