package com.wenliuniversity.springbootTest.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.jcache.config.JCacheConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

import static org.springframework.data.redis.cache.RedisCacheManager.*;

/**自定义redisTemplate的一些配置，将序列化规则改为json
 *    可以使用Jackson2JsonRedisSerializer<>(Object.class) 但是在取出数据到JavaBean时报错：java.util.LinkedHashMap cannot be cast to
 *    原因：序列化带泛型的数据时，会以map的结构进行存储，反序列化是不能将map解析成对象。该序列化器的缓存规则使用 jackson 来做数
 * 据的序列化与反序列化，如果默认使用 Object 作为序列化与反序列化的类型，则其只能识别 java 基本类型，遇到复杂类型如Map时，jackson 就会
 * 先序列化成 LinkedHashMap ，然后再尝试强转为所需类别，这样大部分情况下会强转失败。（）
 *    此时就需要指定序列化方式为:GenericJackson2JsonRedisSerializer
 * 指定后，在序列化时，会将类名存入到序列化后的 json 字符串中，如：
 * {"@class": "com.wenliuniversity.springbootTest.entity.Department", "id" : 1,"departmentName": "人力资源" }
 * 这样在取出缓存时，springboot 就可以自动根据 @class 对应的字段找到对应的类进行反序列化了
 *   原因：使用GenericJackson2JsonRedisSerializer序列化时，会保存序列化的对象的包名和类名，反序列化时以这个作为标示就可以反序列化成指定的对象。
 *   比较：
 *   使用Jackson2JsonRedisSerializer需要指明序列化的类Class，可以使用Object.class，但是为了反序列化不报错，需要指定被序列化的类类型，就不能再写object.class了
 *   使用GenericJacksonRedisSerializer比Jackson2JsonRedisSerializer效率低，占用内存高。
 * @author HuangHai
 * @date 2021/4/19 20:48
 */
@Configuration
public class MyRedisConfig {


    /**
     *    redisTemplate在springboot1.0是定制redisManager的参数，但是在2.0里只用于手动使用redisTemplate存入值有用，但最好也也使用
     * GenericJackson2JsonRedisSerializer 这个序列化器，这样就可以不用怕出现异常而专门为某个bean定制Jackson2JsonRedisSerializer了
     * @param redisConnectionFactory
     * @return
     * @throws UnknownHostException
     */
    //一般只有手动代码写入缓存才会用到redisTemplate,我们之所以还要重写RedisCacheManager，
    // 是因为我们大多数会用注解的方式写入缓存，而那时用到的规则就是RedisCacheManager里配的
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();//直接new一个redisTemplate
        template.setConnectionFactory(redisConnectionFactory);//设置它与redis的连接机制
        //重写javaBean的序列化到redis的规则，使用json，不然都是默认的jdk序列化规则，看不懂。但是反序列化时不能识别复杂JavaBean，会出异常
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        //此时就需要用下面这个
       // GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        template.setDefaultSerializer(serializer);//将这个规则应用到专门缓存对象的redisTemplate,stringRedisTemplate都是字符串不用搞这个
        return template;
    }


    /**
     *   当springboot在进行自动配置时没有发现其他的缓存中间件的场景依赖，且发现容器里没有cacheManager，就会配置默认的
     * SimpleConfiguration配置类，创建一个cacheManager到容器里。如果我们导入了redis场景依赖，就会使用配置RedisConfiguration，
     * 发现容器里没有cacheManager，就会自己配置默认的。，但是如果我们配置了RedisCacheManager到容器里，就会使用我们配置了的。
     *   名字叫redisCacheManager，就会替换RedisConfiguration里创建的。名字叫cacheManager就直接用，不需要创建了。而我们就在我们自己
     * 创建的redisCacheManager或cacheManager里设置序列化器就好。
     * @param factory
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        //new 一个redis序列化器，用String的实现类向上转型，这样就序列化成字符串了
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();

        //Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        /*//解决查询缓存转换异常的问题，使用Jackson2的redis序列化器时用这个
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        jackson2JsonRedisSerializer.setObjectMapper(om);*/

        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                //设置使用redisSerializer序列化键key
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))

                //设置使用jackson2JsonRedisSerializer的序列化机制来序列化值value
                //.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))

                //设置使用genericJackson2JsonRedisSerializer的序列化机制来序列化值value
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericJackson2JsonRedisSerializer))

                //设置缓存禁用空值
                .disableCachingNullValues();

        //设置缓存过期时间
        // redisCacheManager.;

        //使用RedisCacheManagerBuild的build（）方法创建RedisCacheManager
        RedisCacheManager redisCacheManager = builder(factory).cacheDefaults(config).build();
        return  redisCacheManager;
    }

}
