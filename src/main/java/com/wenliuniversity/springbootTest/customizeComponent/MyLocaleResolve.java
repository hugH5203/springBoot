package com.wenliuniversity.springbootTest.customizeComponent;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Locale;

/**进行国际化的转换，这个类在config包下的MyMvcConfig类里使用了@Bean注解，加入了容器里。所以别疑惑为什么不加@Bean也能起作用
 * @author HuangHai
 * @date 2021/4/11 19:20
 */
public class MyLocaleResolve implements LocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        //这种域传递也可以用来进行国际化的转换，但是由于resolveLocale会在controller存值之前就进行检查，导致那时域中还没有值，所以我们要存在session域里
        String language = (String) httpServletRequest.getSession().getAttribute("language");
        //String language=httpServletRequest.getParameter("language");//用于国际化的转换，只需要传参数就可以了，resolveLocale会在一开始就进行检查
        Locale locale = Locale.getDefault();
        if (language!=null){
            String[] split=language.split("_");
            locale= new Locale(split[0],split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
