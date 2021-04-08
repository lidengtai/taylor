package com.tai.java8;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 学生实体类
 * @author: Taylor
 * @date :  2021-03-01 11:02
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    private Long id;
    private String studentName;
    private Integer age;

}
