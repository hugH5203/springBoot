package com.wenliuniversity.springbootTest.controller;

import com.wenliuniversity.springbootTest.exception.MyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuangHai
 * @date 2021/4/13 20:09
 */

/**
 * 全局异常处理
 */
@ControllerAdvice  //全局controller的异常处理或全局数据绑定
public class MyExceptionHandler {


    /*在这里我发现一个很奇怪的问题，这个方法参数里加了Map就会报错，这个方法直接不运行了。
    查询原因说是方法参数不能有model与map等等，否则这个方法直接用不了，根本就不会进入这个方法*/

    //用这个注解会在controller发生这个异常时触发(为了测试其他方法于是我又加了两个大异常)
    @ExceptionHandler({MyException.class,Exception.class,RuntimeException.class})
    public String exceptionHandle(Exception e, HttpServletRequest request){
        HashMap<String,Object> hashMap=new HashMap<>();
       request.setAttribute("javax.servlet.error.status_code",500);
        hashMap.put("exception",e.toString().split(":")[0]);
        hashMap.put("message",e.getMessage());
        request.setAttribute("ext",hashMap);
        return "forward:/error";
    }
}
