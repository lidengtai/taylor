package com.tai.juc;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @description: 线程安全的时间工具类
 * @author: Taylor
 * @date :  2021-04-23 08:22
 **/
public class SafeTimeUtil {

    @Test
    public void test(){
        //线程安全
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        //非线程安全
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");

        LocalDateTime localDateTime = parseStringToDateTime("2021-04-23 12:23:12.000312", "yyyy-MM-dd HH:mm:ss.SSSSSS");

        System.out.println(localDateTime);
        long timestamp = getTimestampOfDateTime(localDateTime);

        System.out.println(timestamp);

        LocalDateTime dateTimeOfTimestamp = getDateTimeOfTimestamp(timestamp);
        System.out.println(dateTimeOfTimestamp);

        String format = dateTimeOfTimestamp.format(dateTimeFormatter);
        System.out.println(format);
    }



    /**
     * 1.将LocalDateTime转为自定义的时间格式的字符串
     * @param localDateTime
     * @param format
     * @return
     */
    public static String getDateTimeAsString(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }

    /**
     * 2.将long类型的timestamp转为LocalDateTime
     * @param timestamp
     * @retur
     */
    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }


    /**
     * 3.将LocalDateTime转为long类型的timestamp
     * @param localDateTime
     * @return
     */
    public static long getTimestampOfDateTime(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }


    /**
     * 4.将某时间字符串转为自定义时间格式的LocalDateTime
     * @param time
     * @param format
     * @return
     */
    public static LocalDateTime parseStringToDateTime(String time, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(time, df);
    }
}
