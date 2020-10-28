package com.taylor.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @description: spark WordCount demo
  * @author: Taylor
  * @date :  2020-10-27 09:39
  **/
object SparkDemo {
  def main(args: Array[String]): Unit = {
    val config : SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")

    val sc = new SparkContext(config)

    val lines: RDD[String] = sc.textFile("in")

    val words: RDD[String] = lines.flatMap(_.split(" "))

    val wordToOne: RDD[(String, Int)] = words.map((_,1))

    val result: RDD[(String, Int)] = wordToOne.reduceByKey(_+_)
    result.collect().foreach(println)
  }
}
