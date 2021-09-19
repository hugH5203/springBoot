package com.wenliuniversity.springbootTest.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.GenericMessageConverter;

/**
 * 设置一些对使用AMQP协议的消息队列进行自定义设置
 * @author HuangHai
 * @date 2021/4/20 19:31
 */
@Configuration
public class MyAMQPConfig {

    /**
     *   会按照json的格式存与取数据，但是如果不是按照json存入的（比如说自己在15672端口存的,Jackson2JsonMessageConverter就不能
     * 识别出来，会报异常说不能转换为json格式）。但还是会消耗队列里的一条消息
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return  new Jackson2JsonMessageConverter(); //设置消息的传送序列化使用json格式
        //return new GenericMessageConverter();  //
    }
}
