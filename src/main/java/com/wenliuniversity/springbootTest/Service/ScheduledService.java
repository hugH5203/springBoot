package com.wenliuniversity.springbootTest.Service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**测试定时任务的注解@Scheduled（需要在入口核心类里使用@EnableScheduling来启用该功能）
 * 被该注解标记的方法会根据参数cron的规则自动执行。因为我们项目开发会需要执行一些定时任务，比如每天凌晨分析前一天的
 * 日志等等。spring也会使用异步执行任务调度的方式执行，不会影响主线程的。
 * @author HuangHai
 * @date 2021/5/12 11:29
 */

@Service
public class ScheduledService {
    /* cron参数的值分别是：秒（0-59），分(0-59)，时(0-23)，日(1-31)，月(1-12)，周(0-7或SUN-SAT，7是SUN)。中间用空格隔开.
    其中任意数值可以用*代替。","代表或，"-"代表区间,"*"代表任意值，"/"代表步长，"?"代表冲突匹配，当日期与星期产生冲突时可用。
    L代表最后，W代表工作日，C代表和calendar（日历）联系过后计算的结果。"#"代表星期，如：4#2即第二个星期三。因为1是星期天。
    * */
    @Scheduled(cron ="0/2 * * * * *" )   //每隔两秒执行一次
    public void scheduled(){
        System.out.println("定时任务执行");  //执行成功！
    }
}
