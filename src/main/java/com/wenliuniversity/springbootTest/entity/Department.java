package com.wenliuniversity.springbootTest.entity;

import java.io.Serializable;

/**
 * @author HuangHai
 * @date 2021/4/17 17:42
 */
public class Department  implements Serializable {

    private Integer id;
    private String departmentName;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getId() {
        return id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
