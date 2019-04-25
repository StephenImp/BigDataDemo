package com.cn.basic2

/**
  * Created by MOZi on 2019/4/19.
  *
  * 模式匹配  match  类似于switch
  *
  * case class(多用在模式匹配中)
  * 构造器中的每一个类型都是val ,不建议用 var
  * 不用new 就可以产生对象(why? apply())
  */
class PatternMatching {


}

case class Book(name:String,author:String)

object PatternMatching extends App{

  val value = 1

   val result = value match {
    case 1 => "one"
    case 2 => "two"
    case _ => "some other number"
  }

  val result1 = value match {
    case i if i ==1 => "one"
    case i if i ==2 => "two"
    case _ => "some other number"
  }

  //println("result of match is :" + result)

  //println("result1 of match is " + result1)


  def t(obj :Any) = obj match {

    case x :Int => println("Int")
    case s :String => println("String")
    case _ =>"other type"
  }

  //t(1)


  /**
    * case class
    */
  val macTalk = Book("MacTalk","CJQ")
  macTalk match {
    case Book(name,author) => println("this is book")
    case _ => println("unknown")
  }

}
