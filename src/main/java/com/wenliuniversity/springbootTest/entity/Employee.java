package com.wenliuniversity.springbootTest.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**测试整合springboot-data的jpa，使用jpa的注解配置相关映射，其实用的就是hibernate框架
 * @author HuangHai
 * @date 2021/4/17 17:42
 */
//@Data//这是lombok插件给我吗提供的一个注解，将其写在类上并导入lombok的依赖，@Data注解就可以自动为我们生成set,get,toString,hashCode,equals等方法
@Entity  //告诉jpa这是一个实体类（和数据库的表映射的类）
@Table(name = "theemployee") //这个注解用来指定和哪个表对应，如果省略就是默认的类名
public class Employee implements Serializable {
    @Id  //指定为表中对应的主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增主键
    private Integer id;

    //@Column(name = "lastName",length = 50) //设置与之对应的表的属性名，省略就默认是实体类的变量名,如果识别不了它会直接修改表中列名
    @Column//不写的话默认是启用驼峰式命名法，在这里其实都可以省略，因为JavaBean的属性与数据库表中的列名是一样的。
    private String lastName;
    @Column
    private Integer gender;
    @Column
    private String email;
    @Column
    private Integer dId;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setdId(Integer dId) {
        this.dId = dId;
    }

    public Integer getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public Integer getdId() {
        return dId;
    }
}
