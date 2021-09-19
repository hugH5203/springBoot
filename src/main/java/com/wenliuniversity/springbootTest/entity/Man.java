package com.wenliuniversity.springbootTest.entity;

import org.springframework.stereotype.Component;

/**
 * @author HuangHai
 * @date 2021/4/6 12:09
 */

@Component("man")
public class Man {
    private String name;
    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Man{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
