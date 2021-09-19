package com.wenliuniversity.springbootTest.mapper;

import com.wenliuniversity.springbootTest.entity.Department;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**整合mybatis，这是一个mapper
 * @author HuangHai
 * @date 2021/4/17 17:44
 */
//@Repository//如果在自动注入mapper的实例报错后，可以写一下该注解。因为@mapper注解毕竟是mybatis提供的，spring可能没有识别
@Mapper//指定这是一个Mapper，并交于spring管理。如果写了这个就可以不用写@MapperScan了。反之写了@MapperScan就可以不用写@Mapper了。
public interface DepartmentMapper {

    @Select("select * from department")
    public List<Department> findAll();

    @Select("select * from department where id=#{id}")
    public  Department findById(Integer id);

    @Delete("delete from department where id=#{id}")
    public int deleteById(Integer id);

    @Update("update department set departmentName=#{department.departmentName} where id=#{id}")
    public int updateById(Department department,Integer id); //当参数较多时，且方法参数列表没有该占位符时，需要进一步指定是谁的属性或用@Parma指定，不然识别不了

    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id") //声明两方的主键，数据库自动生成主键的值且注入到JavaBean的主键中
    @Insert("insert into department(departmentName) values(#{departmentName})")
    public int insertDepartment(Department department);

    @Select("select * from department where departmentName like #{name}")
    public List<Department> findAllByChar(@Param("name") String name1,Integer xxx,String name2);//测试用@Parma
}
