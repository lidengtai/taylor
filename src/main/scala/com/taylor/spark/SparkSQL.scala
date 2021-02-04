package com.taylor.spark

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * @description: sparksql demo
  * @author: Taylor
  **/
object SparkSQL {

  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("SparkSQL")
//      .config("spark.driver.extraClassPath", sparkClassPath)
      .getOrCreate()

    val df: DataFrame = spark.read.format("json").load("in/people.json")

    df.printSchema()

    df.show()

    spark.stop()
  }
}
