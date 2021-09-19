/*
package com.wenliuniversity.springbootTest.config;

import com.wenliuniversity.springbootTest.myOldWeb.MyFilter;
import com.wenliuniversity.springbootTest.myOldWeb.MyListener;
import com.wenliuniversity.springbootTest.myOldWeb.MyServlet;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

*/
/**这里注册java的三大组件，springboot默认没注册，但有它们的自动配置类，我们只需要注册就好
 * @author HuangHai
 * @date 2021/4/15 15:31
 *//*


@Configuration
public class TheOldServlet {

    @Bean  //注册老式servlet
    public ServletRegistrationBean myServlet() { //注册哪个servlet类是老式，并将servlet类与对应路径映射，
        // 映射路径可以写多个，且必须写/，有项目路径得写项目路径。它是不会被拦截器所拦截，因为拦截器只拦截controller的虚拟路径
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new MyServlet(),"/myServlet");
        registrationBean.setLoadOnStartup(1);//设置servlet的启动时机
        return registrationBean;
    }

    @Bean//注册老式过滤器
    //过滤器与拦截器同时存在时，先执行过滤器后执行拦截器，过滤器优先级 > 拦截器
    public FilterRegistrationBean myFilter(){
        FilterRegistrationBean filterRegistrationBean= new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new MyFilter());
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        //filterRegistrationBean.setUrlPatterns(Arrays.asList("/hello","/ddd"));//这个方法参数是一个collection集合，所以我转成了list集合
        return filterRegistrationBean;
    }

    @Bean//注册老式监听器
    public ServletListenerRegistrationBean myListener(){
        ServletListenerRegistrationBean servletListenerRegistrationBean=new ServletListenerRegistrationBean<>(new MyListener());
        return servletListenerRegistrationBean;
    }

    @Bean  //自定义嵌入式的servlet容器的相关规则
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryWebServerFactoryCustomizer(){
        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
            @Override  //设置Tomcat这个servlet容器的访问端口号，优先级最高，大于主配置文件
            public void customize(ConfigurableWebServerFactory factory) {
                factory.setPort(8848);
            }
        };
    }
}
*/
