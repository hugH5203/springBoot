package com.wenliuniversity.springbootTest.config;

import com.wenliuniversity.springbootTest.customizeComponent.MyLocaleResolve;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.TomcatWebSocketServletWebServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**对springboot的MVC自动配置进行修改或替换
 * @author HuangHai
 * @date 2021/4/11 15:35
 */

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {  //实现这个接口可以将springboot许多默认的设置替换成我们想要的特色

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {  //注册表设置
        registry.addViewController("/go").setViewName("success"); //将这个地址在解析的时候替换成另一个地址
    }

    @Bean  //又搞了一个webmvcconfigurer组件到容器里，这些组件springboot会让他们一起起作用，而不像有些是直接替换
    public WebMvcConfigurer webMvcConfigurer(){
        WebMvcConfigurer webMvcConfigurer = new WebMvcConfigurer(){
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/do").setViewName("success");
            }
        /*    @Override //注册拦截器啊
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                        .excludePathPatterns("/index","/","/login");
            }*/
        };
        return webMvcConfigurer ;
    }




    @Bean  //用于处理国际化等功能
    public LocaleResolver localeResolver(){ //妈的，方法名千万不要大写，老子找了一个多小时的bug
        return new MyLocaleResolve();
    }
}

