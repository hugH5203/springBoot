package com.wenliuniversity.springbootTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wenliuniversity.springbootTest.entity.Department;
import com.wenliuniversity.springbootTest.entity.Person;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
class SpringBootTestApplicationTests {
    //记录器，slf4j的logger
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired  //测试DataSource自动配置是否成功完成
            DataSource dataSource;

    @Autowired  //用于测试yaml里的数据是否绑定到实体类中
            Person person;

    @Autowired  //到如redis的相关场景启动器，就会把相应的配置自动注入且放到容器里，我们只需要取出来注入就好
            StringRedisTemplate stringRedisTemplate;  //专门用来操作redis的字符串类型的模板，k与value都是字符串类型，<String,String>

    @Autowired  //用来操作redis对象类型的模板，k与value都是对象类型<Object，Object>,缓存的序列化类型是按默认的jdk，类型，可以修改成json类型，但需要自己自定义json对象类型
            RedisTemplate redisTemplate;

    @Autowired  //自己重写的RedisTemplate,能缓存为json数据，读取也没问题。
    RedisTemplate<Object, Object> myRedisTemplate;

    @Autowired  //测试rabbitTemplate模板对rabbitMQ操作封装的一些方法
    RabbitTemplate rabbitTemplate;

    @Autowired(required = false)  //这是rabbitMQ管理系统的组件，可以对队列，交换器，绑定等进行创建，删除，绑定等功能
    RabbitAdmin rabbitAdmin;

    @Autowired  //这是管理支持AMQP协议(Advanced Message Queuing Protocol)的组件，也可以实现和rabbitAdmin一样的功能
    AmqpAdmin amqpAdmin;

    @Autowired
    JavaMailSenderImpl javaMailSender;  //获取springboot自动配置的邮件发送实现类(在配置文件里配置了相关属性后就可以获取使用)



    @Test
        //用于测试yaml里的数据是否绑定到实体类中
    void contextLoads() {
        System.out.println(person);
    }

    @Test
//测试日志打印
    void testLog() {
        //日志的级别；
        //由低到高   trace<debug<info<warn<error
        //可以调整输出的日志级别；日志就只会在这个级别以后的高级别生效
        logger.trace("这是trace日志...追踪");
        logger.debug("这是debug日志...调试");
        //SpringBoot默认给我们使用的是info级别的，没有指定级别的就用SpringBoot默认规定的级别；info级别
        logger.info("这是info日志...感兴趣");
        logger.warn("这是warn日志...警告");
        logger.error("这是error日志...错误");
    }

    /**
     * 测试配置的mysql数据库参数是否被自动配置读取并加载创建，并查看是什么类型的数据源，springboot2.0版本默认使用Hikari的数据源
     */
    @Test
    void testDataSource() {
        //打印DataSource类名
        System.out.println(dataSource.getClass()); //修改后：class com.alibaba.druid.pool.DruidDataSource
        //打印DataSource创建的连接对象并关闭
        try {
            Connection connection = dataSource.getConnection();
            System.out.println(connection);   //com.mysql.cj.jdbc.ConnectionImpl@106d77da
            connection.close();//关闭连接
        } catch (SQLException e) {
            e.getMessage();
            System.out.println("获取连接失败");
        }
    }


    /**
     * 测试redis的手动缓存，了解StringRedisTemplate与redisTemplate的使用方法,以及序列化到redis里。
     * （被序列化的类必须实现了Serializable接口，否则该类根本不允许被序列化）
     * 可以试试手动转化json然后插入redis里。（刚刚测试了，手动转化json也不行，只能重写序列化规则）
     * 或者自定义一个redisCacheManage来重写它的序列化规则。
     * 当我们引入了redis的启动场景时，springboot就不会使用默认的SimpleConfiguration配置类去注册cacheManager，
     * 用它去创建concurrentMapCache缓存组件，而是使用RedisConfiguration去注册redisCacheManager再用它去创建redisCache，
     * 通过redisCache操作redis缓存数据
     */
    @Test
    public void testRedisStringTemplate() {

        //测试将字符串写入到redis缓存中间件里
        ValueOperations<String, String> redisString = stringRedisTemplate.opsForValue();
        redisString.append("springTem", "这是stringTemplate写入的缓存");//后面我在redis客户端里加入点数据，看看能不能读出来
        System.out.println(redisString.get("springTem")); //输出：这时stringTemplate写入的缓存在redis客户端也查看到了
        stringRedisTemplate.opsForList().leftPushAll("bootList", "1", "2", "3", "4");//存储list<String>类型
        stringRedisTemplate.opsForSet().add("bootSet", "q", "w", "e", "r");//无序集合set<String>
        stringRedisTemplate.opsForZSet().add("bootZset", "a", 1);//有序集合
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("hashKey", "hashValue");
        stringRedisTemplate.opsForHash().putAll("bootHash", hashMap);//hashMap


        //测试将对象类型写入到redis缓存中间件里
        Department department = new Department();
        department.setDepartmentName("运营部");
        department.setId(1);
        redisTemplate.opsForSet().add("redisObject", department);//将对象存储到redis数据库里，默认使用自带jdk序列化规则

        //使用json转换工具类，可以将JavaBean与json互相转换，然后存储到redis里，或者读出来。我用的是fastJson，需要导入相关依赖
        String jsonString = JSON.toJSONString(department); //将对象转化为json字符串
        JSONObject jsonObject = JSONObject.parseObject(jsonString);//将json字符串转化为object格式
        Object parse = JSONObject.parse(jsonString);
        Object o = JSON.toJSON(department);
        System.out.println(jsonString);//{"departmentName":"运营部","id":1}
        System.out.println(jsonObject); //{"departmentName":"运营部","id":1}
        System.out.println(parse);
        System.out.println(o);  //{"departmentName":"运营部","id":1}

        //这种方法解决不了问题，redis内部还是会将转化好了的json用内部默认的jdk序列化规则去存储
        redisTemplate.opsForSet().add("jsonObject", jsonString);
    }


    /**
     * 测试一下是否用到了我自己定义的redisTemplate,是否在redis里真的转化成了json
     * 是的，确实成功了！
     */
    @Test
    public void testCustomizeRedisTemplate() {
        Department department = new Department();
        department.setDepartmentName("公关部");
        department.setId(8);
        myRedisTemplate.opsForSet().add("myRedisTemplate",department);
       // myRedisTemplate.opsForValue().set("myRedisTemplate",department);//这样也能存值
    }

    /**
     * 使用rabbitTemplate进行点对点传递消息
     *direct类型的交换器，会严格按照路由键去寻找它所绑定的队列进行传递消息
     */
    @Test
    public void testRabbitTemplate_direct(){
        //第一个常用方法，传入交换器名字，路由键，和一个Message对象（message对象要自己定义消息头与消息体）
        //rabbitTemplate.send(String exchange,String routingKey,Message message);

        //第二个常用方法，最后传入一个要发送的对象，然后rabbitTemplate会自动将其序列化然后发给rabbitMQ
        //rabbitTemplate.convertAndSend(String exchange,String routingKey,Object object);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("msg","direct交换器第六条消息");
        hashMap.put("data", Arrays.asList("helloWorld!",666,true));
        rabbitTemplate.convertAndSend("my_exchange_direct","direct",hashMap);//hashMap会被序列化后发送给rabbitMQ
        //发送成功！在与该交换器用"direct"路由键绑定的队列queue.direct中可以查看到被序列化的数据
    }

    /** 类似于广播传递消息
     *fanout类型的交换器，会给它所有绑定了的队列发送消息（它不会管路由键匹不匹配）
     */
    @Test
    public void testRabbit_fanout(){
        /*HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("msg","fanout交换器给所有绑定的队列发送消息");
        hashMap.put("data", Arrays.asList("helloWorld!",123,true));*/
        Department department = new Department();
        department.setId(666);
        department.setDepartmentName("睾丸部");
        rabbitTemplate.convertAndSend("my_exchange_fanout","no_need_routingKey",department);
    }


    /**
     * 模拟一个消费者，从队列里拿出消息
     * 使用rabbitTemplate接收队列中的消息(会根据队列的数据结构特点，一条一条拿，先进先出)
     *   topic类型的交换器，运行路由键进行模糊匹配。例如：#.routingKe，"#"代表0个或多个字母，支持路由键后缀为topic的一切键。还有一个是"*"代表一个字母。
     *也是按照路由键来匹配消息队列。
     */
    @Test
    public void testRabbitTemplate_receive(){
        //使用这个方法可以根据传入的队列名拿到其中的消息并反序列化
         //Object o=rabbitTemplate.receiveAndConvert(String queueName)

        //Object o = rabbitTemplate.receiveAndConvert("queue.direct");//一条一条取出queue.direct里的消息
        Object o = rabbitTemplate.receiveAndConvert("queue.fanout");//一条一条取出queue.fanout队里里的消息
        //Object o = rabbitTemplate.receiveAndConvert("queue.topic");//一条一条取出queue.topic队里里的消息
        System.out.println(o.getClass());
        System.out.println(o.toString());

    }


    /**
     *   使用rabbitAdmin对交换器，队列，绑定进行操作
     *   AMQP协议(Advanced Message Queuing Protocol)即：高级消息队列协议。rabbitMQ是基于AMQP实现的。
     *   AMQP协议兼容JMS(Java Message Service),JMS是jvm消息代理的规范。ActiveMQ与HornetMQ就是基于JMS实现的。
     *   AMQP比JMS更强，两者的区别：
     *       JMS是java api，没有跨平台性与跨语言性，提供两种消息模型，支持多种消息类型。
     *      AMQP是网络线级协议，跨平台，跨语言，提供五种消息模型（我们一般大多用三种：direct，fanout，topic），在遇见
     * 复杂的消息时可以进行序列化后发送。
     *
     */
    @Test
    public void testRabbitMQAdmin(){
        //新建一个交换器
        rabbitAdmin.declareExchange(new DirectExchange("springboot_directChange"));
        //新建一个队列
        rabbitAdmin.declareQueue(new Queue("springboot_01Queue"));
        //将新建的交换器与队列进行绑定（要指定交换器，队列，以及绑定的设备的类型（目的地是队列就写队列类型）,还有要携带的参数（没有就写null））
        rabbitAdmin.declareBinding(new Binding("springboot_01Queue",Binding.DestinationType.QUEUE,
                "springboot_directChange","springboot_01withDirectChange",null));
    }


    /**
     * 测试简单邮件的发送
     */
    @Test
    public void simpleMailSend(){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage(); //创建一个简单邮件发送类，可以发送简单邮件

        //邮件设置
        simpleMailMessage.setSubject("一封简单邮件"); //设置邮件主题
        simpleMailMessage.setText("springboot发送了几封简单邮件？");//设置邮件内容
        simpleMailMessage.setTo("1936489510@qq.com");//设置邮件发送目的邮箱，可以是多个邮箱，setTo有一个重载方法可以发多个
        //simpleMailMessage.setTo("1878549056@qq.com");//设置邮件发送目的邮箱，可以是多个邮箱，setTo有一个重载方法可以发多个
        simpleMailMessage.setFrom("1936489510@qq.com");//设置邮件来源，格式必须是邮箱且必须是你配置里授权的邮箱，否则报错（来自地址的邮件必须与授权用户相同。）

        javaMailSender.send(simpleMailMessage);//将设置好的简单邮件对象传给java邮件传送者，然后发送出去
        javaMailSender.send(simpleMailMessage);//发送第二封

    }

    /**
     * 测试复杂邮件发送，可以发html，附件等复杂类型
     */
    @Test
    public void complexMailSend() throws MessagingException {
        //根据java邮件发送者创建一个复杂消息邮件对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        //因为复杂消息邮件对象没有一些设置的方法，所以我们new一个帮助对象
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);//对mimeMessage进行改造且允许上传附件
          //接下来是邮件设置
        mimeMessageHelper.setSubject("一封复杂消息邮件"); //设置主题
        //setText里的true指允许写html
        mimeMessageHelper.setText("<b style='color:blue'>好久不见！</b></br><a href=https://www.pinduoduo.com/>是兄弟就来砍我！</a>",true);
        mimeMessageHelper.setTo("1936489510@qq.com");//目的邮箱
        mimeMessageHelper.setFrom("1936489510@qq.com");//邮件来源
        //上传文件等附件
        mimeMessageHelper.addAttachment("随便一张图.jpg",new File("C:\\Users\\hh176\\Desktop\\捕获2.JPG"));
        //交给javaMailSender发送邮件
        javaMailSender.send(mimeMessage);
        javaMailSender.send(mimeMessage);//发送第二封

/*//恶搞
        mimeMessageHelper.setText("<b style='color:blue'>好久不见！</b></br><a href=https://item.jd.com/10024152121356.html/>猜你喜欢！</a>",true);
        mimeMessageHelper.setTo("1878549056@qq.com");
        mimeMessageHelper.setFrom("1936489510@qq.com");
        mimeMessageHelper.addAttachment("你的最爱.jpg",new File("C:\\Users\\hh176\\Desktop\\裤子.JPG"));
        for (int i = 0; i < 100; i++) {
            javaMailSender.send(mimeMessage);
        }//速度超级超级慢，感觉要半个小时才能发*/
    }

}
