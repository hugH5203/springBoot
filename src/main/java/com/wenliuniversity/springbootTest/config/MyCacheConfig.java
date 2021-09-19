package com.wenliuniversity.springbootTest.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;


/**自定义cache的一些配置
 *     自定义的cache组件里key生成策略类名
 * @author HuangHai
 * @date 2021/4/18 21:32
 */
@Configuration
public class MyCacheConfig {

    @Bean("myKeyGenerator")  //设置自定义的cache组件里key生成策略类名
    public KeyGenerator keyGenerator(){  //配置自定义的cache组件的key生成策略
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                return method.getName()+"*"+ Arrays.asList(objects).toString()+"*"; //自定义key为：方法名*方法的所有参数*
                //也可以返回这种键
               /* StringBuilder sb = new StringBuilder();
                sb.append(o.getClass().getName()).append(".").append(method.getName()).append(Arrays.toString(objects));
                return sb.toString();*/
            }
        };

        }
    }


