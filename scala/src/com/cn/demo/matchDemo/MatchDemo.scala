package com.cn.demo.matchDemo

/**
  * Created by MOZi on 2019/5/22.
  *
  * 1.概念理解：
      Scala 提供了强大的模式匹配机制，应用也非常广泛。
      一个模式匹配包含了一系列备选项，每个都开始于关键字 case。
      每个备选项都包含了一个模式及一到多个表达式。箭头符号 => 隔开了模式和表达式。
    2.代码及注意点
      模式匹配不仅可以匹配值还可以匹配类型
      从上到下顺序匹配，如果匹配到则不再往下匹配
      都匹配不上时，会匹配到case _ ,相当于default
      match 的最外面的”{ }”可以去掉看成一个语句
  */
class MatchDemo {

  /**
    * 注意点：
    * 1.模式匹配不仅可以匹配值，还可以匹配类型
    * 2.模式匹配中，如果匹配到对应的类型或值，就不再继续往下匹配
    * 3.模式匹配中，都匹配不上时，会匹配到 case _ ，相当于default
    */
  def matchTest(x:Any) ={
    x match {
      case x:Int=> println("type is Int")
      case 1 => println("result is 1")
      case 2 => println("result is 2")
      case 3=> println("result is 3")
      case 4 => println("result is 4")
      case x:String => println("type is String")
      //      case x :Double => println("type is Double")
      case _ => println("no match")
    }
  }

}
object MatchDemo{

  def main(args: Array[String]): Unit = {
    val tuple = Tuple6(1,2,3f,4,"abc",55d)
    val tupleIterator = tuple.productIterator
    while(tupleIterator.hasNext){
      val matchDemo =  new MatchDemo
      matchDemo.matchTest(tupleIterator.next())
    }
  }

}
