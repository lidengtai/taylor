package com.taylor.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @description: RDD demo
  * @author: Taylor
  * @date :  2020-10-27 15:17
  **/
object SparkRDD {

  def main(args: Array[String]): Unit = {

    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SPARK-RDD-DEMO")

    val sc = new SparkContext(sparkConf)

    //创建RDD: 从内存中创建makeRDD 底层实现parallelize
//    val listRDD: RDD[Int] = sc.makeRDD(List(1,2,3,4,5,6,7,8,9,10,11))

    //从外部存储中创建
    val fileRDD: RDD[(String, Int)] = sc.textFile("in",4)
      .flatMap(_.split(" "))
      .map((_,1))
      .reduceByKey(_+_)

    fileRDD.saveAsTextFile("output")
  }
}
