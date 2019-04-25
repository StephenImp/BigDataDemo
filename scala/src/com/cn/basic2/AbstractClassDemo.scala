package com.cn.basic2

/**
  * Created by MOZi on 2019/4/19.
  * 抽象类
  *
  * trait  类比java  interface    带有具体实现方法的接口
  */
class AbstractClassDemo {

}

abstract class Person2{
  def speak;
  val name :String
  var age:Int
}

/**
  * 继承抽象类
  */
class Student1 extends Person2{

  /**
    * 无返回值
    */
  override def speak{
  println("speak!!!")
  }

  /**
    * 为了对比上面
    * 这里是有返回值的
    */
  def speak1 = {
  }

  val name = "wpw"
  var age = 27

}

/**
  * trait ①
  */
trait Logger{

  def log(msg:String){
    println("log" +":"+ msg)

  }

}

class Test extends Logger{

  def test: Unit ={
    log("xxx")
  }
}

/**
  * trait ②
  */
trait Logger1{
  def log(msg:String)
}

trait ConsoleLogger1 extends Logger1{
  def log(msg:String){
    println(msg)
  }
}

class Test1 extends ConsoleLogger1{

  def test{
    log("PPP")
  }
}

/**
  * trait ③
  */
trait ConsoleLogger{
  def log(msg:String){
    println("save money:" + msg)
  }
}

trait MessageLogger extends ConsoleLogger{

  override def log(msg : String){
    println("save money to bank :"+msg)
  }
}

abstract class Account{
  def save
}

class MyAcount extends Account with ConsoleLogger{

  override def save{
    log("100")
  }
}


object AbstractClassDemo extends App{

  //抽象方法
//  val s = new Student1
//  s.speak

  //抽象属性
//  println(s.name +":"+s.age)

  //trait  类比java  interface    带有具体实现方法的接口
  //trait ①
  val t = new Test
  t.test

  //trait ②
  val t1 = new Test1
  t1.test

  //trait ③
  val acc = new MyAcount
  acc.save

  //trait ④   with
  val acc1 = new MyAcount with MessageLogger
  acc1.save


}