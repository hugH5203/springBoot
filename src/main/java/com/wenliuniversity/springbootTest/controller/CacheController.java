package com.wenliuniversity.springbootTest.controller;

import com.wenliuniversity.springbootTest.Service.CacheService;
import com.wenliuniversity.springbootTest.entity.Department;
import com.wenliuniversity.springbootTest.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测试spring的Cache是否成功
 *
 * @author HuangHai
 * @date 2021/4/18 20:59
 */
@RestController
public class CacheController {

    @Autowired
    CacheService cacheService;

    @GetMapping("cacheFindAll")
    public List<Department> cacheFindAll() {
        return cacheService.findAll();
    }

    @GetMapping("cacheFindOne/{id}")
    public Department cacheFindOne(@PathVariable("id") Integer integer){
        return cacheService.findByid(integer);
    }
}
