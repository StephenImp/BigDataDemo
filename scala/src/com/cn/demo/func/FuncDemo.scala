package com.cn.demo.func

import java.util.Date

/**
  * Created by MOZi on 2019/5/14.
  */

/**
  *   1.函数定义语法 用def来定义
  *
      2.可以定义传入的参数，要指定传入参数的类型

      3.方法可以写返回值的类型也可以不写，会自动推断，
        有时候不能省略，必须写，比如在递归函数中或者函数的返回值是函数类型的时候。

      4.scala中函数有返回值时，可以写return，也可以不写return，会把函数中最后一行当做结果返回。当写return时，必须要写函数的返回值。

      5.如果返回值可以一行搞定，可以将{}省略不写

      6.传递给方法的参数可以在方法中使用，并且scala规定方法的传过来的参数为val的，不是var的。

      7.如果去掉方法体前面的等号，那么这个方法返回类型必定是Unit的。这种说法无论方法体里面什么逻辑都成立，
        scala可以把任意类型转换为Unit.假设，里面的逻辑最后返回了一个string，那么这个返回值会被转换成Unit，并且值会被丢弃。
  */
class FuncDemo {

  /**
    * ①Scala函数的定义
    */
  def fun (a: Int , b: Int ) : Unit = {
    println(a+b)
  }
  fun(1,1)

  def fun1 (a : Int , b : Int)= a+b
  println(fun1(1,2))

}


object FuncDemo{

  def main(args: Array[String]): Unit = {


    /**
      * ①Scala函数的定义
      */
    //val funcDemo = new FuncDemo

    /**
      * ②
      * 递归函数
      * 5的阶乘
      */
//    def fun2(num :Int) :Int= {
//      if(num ==1)
//        num
//      else
//        num * fun2(num-1)
//    }
//    print(fun2(5))


    /**
      * ③
      * 包含默认参数值的函数
      * 注意：
      * 1.默认值的函数中，如果传入的参数个数与函数定义相同，则传入的数值会覆盖默认值
      * 2.如果不想覆盖默认值，传入的参数个数小于定义的函数的参数，则需要指定参数名称
      */
//    def fun3(a :Int = 10,b:Int) = {
//      println(a+b)
//    }
//    fun3(3,b=2)

    /**
      * ④
      * 可变参数个数的函数
      * 注意：多个参数逗号分开
      */
//    def fun4(elements :Int*)={
//      var sum = 0;
//      for(elem <- elements){
//        sum += elem
//      }
//      sum
//    }
//    println(fun4(1,2,3,4))

    /**
      * ⑤
      * 匿名函数
      * 1.有参数匿名函数
      * 2.无参数匿名函数
      * 3.有返回值的匿名函数
      * 注意：
      * 可以将匿名函数返回给定义的一个变量
      */
    //有参数匿名函数
//    val value1 = (a : Int) => {
//      println(a)
//    }
//    value1(1)

    //无参数匿名函数
//    val value2 = ()=>{
//      println("wpw")
//    }
//    value2()

    //有返回值的匿名函数
//    val value3 = (a:Int,b:Int) =>{
//      a+b
//    }
//    println(value3(4,4))


    /**
      * ⑥
      * 嵌套函数
      * 例如：嵌套函数求5的阶乘
      */
//    def fun5(num:Int)={
//      def fun6(a:Int,b:Int):Int={
//        if(a == 1){
//          b
//        }else{
//          fun6(a-1,a*b)
//        }
//      }
//      fun6(num,1)
//    }
//    println(fun5(5))

    /**
      * 偏应用函数
      *
      * 偏应用函数是一种表达式，不需要提供函数需要的所有参数，只需要提供部分，或不提供所需参数。
      */
    def log(date :Date, s :String)= {
      println("date is "+ date +",log is "+ s)
    }

    val date = new Date()
    log(date ,"log1")
    log(date ,"log2")
    log(date ,"log3")

    //想要调用log，以上变化的是第二个参数，可以用偏应用函数处理
    val logWithDate = log(date,_:String)
    logWithDate("logWithDate1")
    logWithDate("logWithDate2")
    logWithDate("logWithDate3")

  }
}
