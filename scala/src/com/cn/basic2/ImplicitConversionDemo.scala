package com.cn.basic2

/**
  * Created by MOZi on 2019/4/19.
  *
  * 隐式转换
  *
  * object ImplicitConversionDemo  是  class ImplicitConversionDemo 的伴生对象
  */
class ImplicitConversionDemo {

}

class A{

}

class RichA(a:A){

  def rich{
    println("rich ...")
  }

}

object ImplicitConversionDemo extends App{

  /**
    * 隐式转换
    * @param a
    * @return
    */
  //把 A 转换成 RichA
  implicit def aToRichA(a:A) = new RichA(a)

  val a = new A
  //a.rich

  /**
    * 隐式参数转换
    *
    * 调用这个方法不需要传参数也可以
    * @return
    */
  def testParam(implicit name: String){
    println(name)
  }

  implicit val name = "implicit!!!"
  //testParam("xx")

  /**
    * 为Int 提供拓展功能
    *
    * 为 Int 类型 的数据 提供 一个新的add()
    */
  implicit class Calculator(x:Int){
    def add(a:Int):Int = a + 1
  }

  //import xx.xx.Calculator   使用的时候要注意导包
  println(1.add(1))
}
