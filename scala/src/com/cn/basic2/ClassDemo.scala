package com.cn.basic2

/**
  * Created by MOZi on 2019/4/19.
  *
  * 类的使用
  */
class Basic2 {

}

class Person{

  //定义变量，但是不赋初始值
  var name: String = _  //var 声明  会生成getter和setter 方法

  val age = 10          //val 声明  只会生成getter方法

  private[this] val gender = "male";//private[this] 代表 gender 只能在 Person 内部使用

}

/**
  * 1.主构造器：参数跟在类名后的,主构造器中的参数最后会被编译成字段
  * 2.主构造器执行的时候，会执行类中的所有语句。
  * 3.假设参数声明时不带val和var，那么相当于private[this]  就只能在内部使用了 ***
  *
  * @param name
  * @param age
  */
class Person1(val name:String ,var age:Int){

  println("this is the primary constructor!!!")

  var gender : String = _

  val school = "SY"
  /**
    * 1.附属构造器名称为this
    * 2.每一个附属构造器必须首先调用已经存在的主构造器或者附属构造器
    * @param name
    * @param age
    * @param gender
    */
  def this(name:String ,age :Int ,gender :String ){
    this(name,age)
    this.gender = gender
  }

}

/**
  * 继承
  */
class Student(name:String,age:Int,val major:String) extends Person1(name,age){
  println("this is the subclass of Person ,major is :" +major)

  /**
    * 重写
    * 父类中有的方法，子类中要去覆盖他，一定要用override
    */
  override def toString = "Override toString..."

  /**
    * 重写父类属性
    */
  override val school = "GZ"
}


object Basic2{

  def main(args: Array[String]){

    //getter和setter  ①
//    val p = new Person()//()可以省略
//    p.name = "jack"
//    println(p.name + ":" + p.age)

    //主 constructor  ②
    //val p  = new Person1("Jack",20)
    //println(p.name+":"+p.age+":"+p.school)

    //附属 constructor  ③
//    val p  = new Person1("Jack",20,"male")
//    println(p.name+":"+p.age+":"+p.gender)

    //继承  ④
    //先运行父构造器，再运行子构造器
    val s = new Student("wpw",27,"Match")

    //重写父类方法  ⑤
    println(s.toString)

    //重写父类属性  ⑥
    //println(s.school)

  }

}
