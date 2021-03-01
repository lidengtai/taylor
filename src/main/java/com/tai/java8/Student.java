package com.tai.java8;

/**
 * @description: 学生实体类
 * @author: Taylor
 * @date :  2021-03-01 11:02
 **/

public class Student {

    private Long id;
    private String studentName;
    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
