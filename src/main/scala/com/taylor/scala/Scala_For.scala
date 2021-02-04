package com.taylor.scala

import scala.util.control.Breaks

/**
  * @description: ${description}
  * @author: Taylor
  * @date :  2020-11-12 11:31
  **/
object Scala_For {

  def main(args: Array[String]): Unit = {

/*    for(i <- 1 to 10){
      println(s"to循环的值：$i ")
    }
    for(i <- 1 until 10){
      println(s"until循环的值：$i")
    }*/


    //杨辉三角
    /**
          *
         ***
        *****
      */
    for (i <- Range(1,18,2)){
      println( " "*((18-i)/2) + "*"*i + " "*((18-i)/2) )
    }


    //类似于continue
    for (i <- 1 to 10 if i%2==0){

        println(i)
    }

    Breaks.breakable()

  }
}
