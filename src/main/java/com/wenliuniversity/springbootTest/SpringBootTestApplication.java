package com.wenliuniversity.springbootTest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.wenliuniversity")//扫描该包与该包下的所有子包里的注解，可以不写，默认是springbootApplication与该包下的字包
//@ServletComponentScan(basePackages = "com.wenliuniversity.springbootTest.myOldWeb")//使用了这个注解就可以用注解注册老式servlet三大组件了：@WebServlet，@WebFilter，@WebListener
//@MapperScan(basePackages = "com.wenliuniversity.springbootTest.mapper") //扫描Mapper所在的包
@EnableCaching//开启springboot的注解缓存
@EnableRabbit//开启基于注解的rabbitMQ模式
@EnableAsync  //开启异步注解功能
//@EnableScheduling  //开启基于注解的定时任务
public class SpringBootTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTestApplication.class, args);
    }

}
