package com.wenliuniversity.springbootTest.controller;

import com.wenliuniversity.springbootTest.entity.Employee;
import com.wenliuniversity.springbootTest.jpaRepository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**这个controller用于测试springboot-data对jpa的一些实现类的使用
 * @author HuangHai
 * @date 2021/4/18 15:48
 */
@RestController
public class JpaController {

    @Autowired //注入对具体表进行操作的实现类
    EmployeeRepository employeeRepository;

    @GetMapping("employee/{id}")
    public List<Employee> getEmployee(@PathVariable("id") Integer id){
        Employee one=new Employee();
        one.setEmail("11123");
        one.setdId(10);
        one.setGender(3);
        one.setLastName("打桩机");

        //employeeRepository.save(one);  //插入,返回插入的对象
        //one.setLastName("搅屎棍");
        employeeRepository.saveAndFlush(one);//会覆盖同一个对象上次插入的数据，返回插入的对象
        Employee employee = employeeRepository.getOne(id);  //根据id查询
        List<Employee> all = employeeRepository.findAll();  //查询所有，返回List集合
        //long count = employeeRepository.count();//返回数据条数
       // employeeRepository.deleteById(9);//根据id删除数据，没有该id会报错
        //employeeRepository.notify();//这是修改方法吗？
        return all;
    }
}
