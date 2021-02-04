package com.taylor.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * @description: sparkstreaming demo
  * @author: Taylor
  * @date :  2020-11-04 16:37
  **/
object SparkStreaming {

  def main(args: Array[String]): Unit = {

    //spark配置对象
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("streaming-wc")

    //实时数据分析：采集数据周期
    val streamingContext = new StreamingContext(sparkConf,Seconds(5))

    //从指定的端口采集数据

    //指定目录采集数据
    val textDStream: DStream[String] = streamingContext.textFileStream("temp/")

    //扁平化
    val worldDStream: DStream[String] = textDStream.flatMap(line=>line.split(" "))

    //转换数据
    val mapDStream: DStream[(String, Int)] = worldDStream.map((_,1))

    //聚合数据
    val sumDStream: DStream[(String, Int)] = mapDStream.reduceByKey(_+_)

    //打印数据
    sumDStream.print()

    //启动采集器
    streamingContext.start()

    //driver等待采集器执行
    streamingContext.awaitTermination()
  }
}
