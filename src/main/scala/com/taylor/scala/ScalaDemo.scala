package com.taylor.scala


/**
  * @description:
  *  1 val 修饰变量 =》java 的 static final  var 定义的变量可变
  *  2
  * @author: Taylor
  * @date :  2020-11-09 10:13
  **/
object ScalaDemo {


  def main(args: Array[String]): Unit = {

    val a = 100
    var b =200
//    a = 300
    b = 400
    println(b)

    /**
      * 参数可变长的函数 *代表多个参数: Any 可传多类型的参数
      * @param s
      */
    def fun(s:Any*): Unit ={
      // 三种方式等价
//      s.foreach(elem => {print(elem)})
//      s.foreach(print(_))
      s.foreach(print)
    }

    fun("lidengtai is taylor；age is ", 30)


    /**
      * 匿名函数
      * ()=>{ }
      *
      * 常用于一个方法的参数时
      *
      */
    (a:Int,b:Int) => {
      a+b
    }


    /**
      * 嵌套方法
      *
      */


  }
}
