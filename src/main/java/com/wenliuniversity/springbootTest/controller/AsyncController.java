package com.wenliuniversity.springbootTest.controller;

import com.wenliuniversity.springbootTest.Service.AsyncService;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**该controller用于测试spring的@Async注解，该注解可以将被标注的方法注为异步方法，spring识别后会自动开启一个新的线程来运行该方法，而不影响
 * 主线程的进度，新线程会在运行完后通知主线程调用。使用户体验更好，不用长时间的等待。
 * @author HuangHai
 * @date 2021/5/12 10:44
 */
@RestController
public class AsyncController {

    @Autowired
    AsyncService asyncService;

    @GetMapping("async")
    public String asyncTask(){
        asyncService.hello();
        return "success";
    }


}
