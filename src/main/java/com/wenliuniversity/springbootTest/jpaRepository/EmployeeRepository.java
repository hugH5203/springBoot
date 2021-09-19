package com.wenliuniversity.springbootTest.jpaRepository;

import com.wenliuniversity.springbootTest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**要使用jpa的实现类实现对数据库的操作，必须继承一些已经写了实现的接口
 * @author HuangHai
 * @date 2021/4/18 15:44
 */
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {  //这里泛型要指定需要操作的具体对应类与表，以及id的类型
    //之所以明明是一个接口却可以不用实现里面的方法就使用，是因为这套api就是springData提供的，springboot会自动识别哪个自定义的类
    // 继承了JpaRepository<>类或它的子类或父类，并检查如果没有重写里面的方法的话就自动装载已实现的方法在里面，供调用者使用
}
