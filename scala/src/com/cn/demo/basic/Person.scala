package com.cn.demo.basic


/**
  * Created by MOZi on 2019/5/14.
  * 注意点：
  *
    ①建议类名首字母大写 ，方法首字母小写，类和方法命名建议符合驼峰命名法。

    ②scala 中的object是单例对象，相当于java中的工具类，可以看成是定义静态的方法的类。object不可以传参数。另：Trait不可以传参数

    ③scala中的class类默认可以传参数，默认的传参数就是默认的构造函数。
    重写构造函数的时候，必须要调用默认的构造函数。

    ④class 类属性自带getter ，setter方法。

    ⑤使用object时，不用new,使用class时要new ,并且new的时候，class中除了方法不执行，其他都执行。

    ⑥如果在同一个文件中，object对象和class类的名称相同，则这个对象就是这个类的伴生对象，这个类就是这个对象的伴生类。可以互相访问私有变量。
  */
class Person(xname :String , xage :Int){

  var name = Person.name
  val age = xage
  var gender = "m"
  def this(name:String,age:Int,g:String){
    this(name,age)
    gender = g
  }

  def sayName() = {
    "my name is "+ name
  }
}

object Person {

  val name = "zhangSanFeng"

  def main(args: Array[String]): Unit = {

    val person = new Person("wagnwu", 10, "f")
    println(person.age);
    println(person.sayName())
    println(person.gender)
  }
}
