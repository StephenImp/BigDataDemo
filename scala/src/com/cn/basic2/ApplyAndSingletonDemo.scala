package com.cn.basic2

/**
  * Created by MOZi on 2019/4/19.
  * apply and 单例
  *
  * object 关键字相当于静态方法   所以用于创建单例
  * apply()  作用: 初始化对象实例
  */
class ApplyAndSingletonDemo {

}

//这个是类
class ApplyTest{

  def apply() = "APPLY"

  def test{
    println("test")
  }
}

//这个是方法
object ApplyTest{

  var count = 0

  def apply() = new ApplyTest

  def static{
    println("I'm a static method")
  }

  def incr = {
    count = count+1
  }

}

object ApplyAndSingletonDemo extends App{

  //ApplyTest.static

  //apply()  作用: 初始化对象实例
  //ApplyTest()  就是调用了apply()
  //类名+() 调用的都是object 的 apply()
  val a = ApplyTest()
  a.test

  //对象+() 调用的是class 的 apply()
  var t = new ApplyTest
  println(t())

  for (i <- 1 to 10 ){
    ApplyTest.incr
  }

  println(ApplyTest.count)

}