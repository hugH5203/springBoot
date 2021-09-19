package com.wenliuniversity.springbootTest.Service;

import com.wenliuniversity.springbootTest.entity.Department;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/** 使用@EnableRabbit＋@RabbitListener注解来监听各个队列的状态
 * 这就相当于消费者，但是会一次性取出所有消息.
 * @author HuangHai
 * @date 2021/4/20 20:27
 */
@Service
public class rabbitMQService {

    @RabbitListener(queues ={ "queue.direct"}) //监听queue.direct消息队列,当该队列收到消息时会触发该方法（可以监听多个队列）
    public void receive(Department department){ //收到的内容如果有Department类型，就会自动填充到该参数里，类似于springMVC
        System.out.println("收到queue.direct里发送的消息："+department); //直接反序列化成发送前的department，
    }

    @RabbitListener(queues = "queue.topic")  //监听这个队列
    public void receive(Message message){  //  将收到的消息存入方法参数里，打印相关信息
        System.out.println(message.getBody());  //打印的是消息的字节数组对象
        System.out.println(message.getMessageProperties());

    }
}
