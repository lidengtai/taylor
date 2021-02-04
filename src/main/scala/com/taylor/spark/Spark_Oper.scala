package com.taylor.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @description: 算子demo
  * @author: Taylor
  * @date :  2020-10-28 14:14
  **/
object Spark_Oper {
  def main(args: Array[String]): Unit = {

    val config : SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")

    val sc = new SparkContext(config)

    val listRDD: RDD[Int] = sc.makeRDD(1 to 10,2)

    /**
      * map是对rdd中的每一个元素进行操作；
      */
    //    val mapRDD: RDD[Int] = listRDD.map(x => x*2)
    val mapRDD: RDD[Int] = listRDD.map(_*2)
//    mapRDD.collect().foreach(println)

    /**
      * mapPartitions则是对rdd中的每个分区的迭代器进行操作
      * mapPartitions可对一个RDD中的所有的分区进行遍历
      * mapPartitions的效率优于map算子，减少了发送到执行器的交互次数
      * 缺点:有可能造成内存溢出
      */
    val mapPartitionsRDD: RDD[Int] = listRDD.mapPartitions(_.map(_*3))
//    mapPartitionsRDD.foreach(println)

    /**
      * 与 mapPartitions 类似
      * 需要提供一个代表分区索引的整型值作为参数
      */
    val indexRDD: RDD[(Int, String)] = listRDD.mapPartitionsWithIndex {
      case (num, datas) => {
        datas.map((_, "分区号：" + num))
      }
    }
    indexRDD.foreach(println)

  }
}
