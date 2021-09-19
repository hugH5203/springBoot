package com.wenliuniversity.springbootTest.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**自定义mybatis的一些默认配置如：javaBean属性与数据库表中字段的映射
 * @author HuangHai
 * @date 2021/4/17 20:40
 */

@Configuration
public class MybatisConfig {

    @Bean
    public ConfigurationCustomizer configurationCustomizer(){
        return new ConfigurationCustomizer() {  //设置字段与属性大小写识别（使用驼峰命名法）。也可以在yml等主配置文件中配置
            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                configuration.setMapUnderscoreToCamelCase(true);
            }
        };
    }
}
