package com.wenliuniversity.springbootTest.Service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**该Service用于测试spring的@Async注解，该注解可以将被标注的方法注为异步方法，
 * spring识别后会自动开启一个新的线程来运行该方法，而不影响
 * 主线程的进度，新线程会在运行完后通知主线程调用。使用户体验更好，不用长时间的等待。
 * @author HuangHai
 * @date 2021/5/12 11:03
 */
@Service
public class AsyncService {

    @Async  //告诉spring这是一个异步方法
    public void hello(){
        try {
            Thread.sleep(3000);  //让线程睡眠三秒，测试有无异步的区别。非异步的话主线程会被阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("测试异步注解");
    }
}
