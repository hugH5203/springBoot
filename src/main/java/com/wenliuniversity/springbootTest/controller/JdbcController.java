package com.wenliuniversity.springbootTest.controller;

import com.wenliuniversity.springbootTest.entity.Department;
import com.wenliuniversity.springbootTest.mapper.DepartmentMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 从mysql里获取数据，测试springboot＋jdbc,springboot＋mybatis
 *
 * @author HuangHai
 * @date 2021/4/17 14:44
 */
@RestController//都将返回到response里，不会自动用前端控制器自动解析字符串到模板引擎
public class JdbcController {

    @Autowired
    JdbcTemplate jdbcTemplate; //jdbc的自动配置会在有数据源后自动将数据源注入到spring的jdbcTemplate里，方便我们直接使用模板来操作数据库

    @Autowired(required = false)  //不写required可能会报红线，因为没有把Mapper加入到容器中，但其实我们加了：@Mapper，只是springboot没有识别这个注解，加上这个属性就好了
    DepartmentMapper departmentMapper;



    //@ResponseBody//将结果返回到response里，不会自动用前端控制器自动解析字符串到模板引擎
    @GetMapping("selectDep")//返回数据第一行
    public Map<String, Object> selectDep() {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT * FROM department");
        return maps.get(2);
    }

    @GetMapping("mybatis")
    public Object mybatis(){
/*        List<Department> findAll = departmentMapper.findAll();

        Department newDepartment01 = new Department();
        newDepartment01.setDepartmentName("财务部");
        departmentMapper.insertDepartment(newDepartment01);

        departmentMapper.deleteById(2);

        Department findOne = departmentMapper.findById(10);

        newDepartment01.setDepartmentName("技术部");
        departmentMapper.updateById(newDepartment01,10);*/

        List<Department> departments = departmentMapper.findAllByChar("%部%", 10,"%ah%");
        return departments;
    }

    @GetMapping("getOne/{id}")
    public Department getOne(@PathVariable("id") Integer id){
        Department one = departmentMapper.findById(id);
        return one;
    }
}
