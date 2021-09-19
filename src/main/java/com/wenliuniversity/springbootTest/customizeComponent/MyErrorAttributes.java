package com.wenliuniversity.springbootTest.customizeComponent;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**  自定义非pc客户端的错误页面信息
 * @author HuangHai
 * @date 2021/4/13 23:07
 */
/*
@Component
public class MyErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        Map<String,Object> map=super.getErrorAttributes(requestAttributes, includeStackTrace);
        return map;
    }
}
*/
