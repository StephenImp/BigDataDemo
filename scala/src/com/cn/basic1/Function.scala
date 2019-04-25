package com.cn.basic1

/**
  * Created by MOZi on 2019/4/19.
  *
  * 这里主要是函数的一些小用法
  *
  * 类只会被编译,不能直接执行,类的声明和主构造器在一起被声明,在一个类中,主构造器只有一个.
  * 类和它的伴生对象可以相互访问其私有成员
  *
  * class和object的一个差别是,
  * 单例对象不带参数,而类可以.
  * 因为你不能用new关键字实例化一个单例对象,你没有机会传递给它参数
  */
class Function {

}

/**
  * 可以理解为Scala把类中的static集中放到了object对象中,
  * 伴生对象和类文件必须是同一个源文件,可以用伴生对象做一些初始化操作.
  */
object Function{

  /**
    * 自定义函数
    * @param name
    * @return 可以省略，最后一行默认return
    *
    *  public static String hello(String name){ return xxx }
    *
    *
    *  ChinaSpark  默认值
    */
  def hello(name:String = "ChinaSpark") :String ={
    "Hello:"+name
  }

  /**
    * 没有参数的时候，
    * 方法的声明和调用都可以不加()
    */
  def helloScala(){
    println("hello scala")
  }

  /**
    *  Unit 相当于 void  关键字
    *
    *  与until 做 区分
    *
    *  until 和 to 的区别  集合的开闭关系
    */
  def helloUntil(): Unit ={
    println("test Unit")
  }

  /**
    * 匿名函数
    * @return
    */
  def add = (x:Int,y:Int) => x + y

  //var add = (x:Int,y:Int) => x + y

  /**
    * 柯里化  是这么写的嘛？
    *
    * 可以只传一个参数？
    *
    * @param x
    * @param y
    * @return
    */
  def add2(x:Int)(y:Int) = x + y


  /**
    *  String*  接收一系列的String类型的参数
    * @param c
    */
  def printEveryChar(c:String*) = {
    c.foreach(x => println(x))
  }


  /**
    * 这里相当于main()函数
    * @param args
    */
  def main(args: Array[String]){

//    print(hello())
//
//    println("Hello Scala")
//    println(hello("scala"))
//
//    helloScala()
//    helloScala
//
//    add(1,2)
//
//    println(add2(1)(2))


    println(add2(1)_)

    //println(add2(1)) //报错？

    //printEveryChar("A","B","C","D","E")

  }
}
