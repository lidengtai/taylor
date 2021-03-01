package com.tai.java8;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: java8集合测试
 * @author: Taylor
 * @date :  2021-03-01 10:49
 **/
public class CollectionDemo {

    @Test
    public void testStreamAndFor() {
        List<Student> studentList = new ArrayList<>();
        // 初始数据量
        int listSize = 1000;
        // 测试次数，以便求出平均运行时长
        int testTimes = 5;
        for (int i = 0; i < listSize; i++) {
                Student student = new Student();
                student.setId(i + 1l);
                student.setStudentName("name" + i);
                student.setAge(i);
                studentList.add(student);
        }
        BigDecimal streamTotalRunTime = new BigDecimal("0");
        BigDecimal forTotalRunTime = new BigDecimal("0");
        for (int i = 0; i < testTimes; i++) {
            Instant streamStart = Instant.now();
            Map<Long, Student> idMapOfStream = studentList.stream()
                    .collect(Collectors.toMap(Student::getId, v -> v));
            List<Integer> studentAgeListOfStream = studentList.stream()
                    .map(Student::getAge)
                    .collect(Collectors.toList());
            long streamRunTime = Duration.between(streamStart, Instant.now()).toMillis();
            System.out.println("第" + (i + 1) + "次：" + "stream 耗时：" + streamRunTime);
            Instant forStart = Instant.now();
            int size = studentList.size();
            Map<Long, Student> idMapOfFor = new HashMap<>(size);
            List<Integer> ageListOfFor = new ArrayList<>();
            for (Student student : studentList) {
                idMapOfFor.put(student.getId(), student);
                ageListOfFor.add(student.getAge());
            }
            long forRunTime = Duration.between(forStart, Instant.now()).toMillis();
            System.out.println("第" + (i + 1) + "次：" + "for 耗时：" + forRunTime);
            streamTotalRunTime = streamTotalRunTime.add(new BigDecimal(streamRunTime + ""));
            forTotalRunTime = forTotalRunTime.add(new BigDecimal(forRunTime + ""));
        }
        System.out.println("list长度为：" + listSize + "， 总共测试次数：" + testTimes);
        System.out.println("stream总运行时间（ms） ：" + streamTotalRunTime);
        System.out.println("for总运行时间（ms） ：" + forTotalRunTime);
        BigDecimal streamAverageRunTime = streamTotalRunTime.divide(new BigDecimal(testTimes + ""), 2, BigDecimal.ROUND_HALF_UP);
        System.out.println("stream平均每次运行时间（ms） ：" + streamAverageRunTime);
        BigDecimal forAverageRunTime = forTotalRunTime.divide(new BigDecimal(testTimes + ""), 2, BigDecimal.ROUND_HALF_UP);
        System.out.println("for平均每次运行时间（ms） ：" + forAverageRunTime);
    }
}
