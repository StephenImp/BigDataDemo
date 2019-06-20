package com.cn.demo.`trait`

/**
  * Created by MOZi on 2019/5/22.
  *
  * 1.概念理解
    Scala Trait(特征) 相当于 Java 的接口，实际上它比接口还功能强大。
    与接口不同的是，它还可以定义属性和方法的实现。
    一般情况下Scala的类可以继承多个Trait，从结果来看就是实现了多重继承。Trait(特征) 定义的方式与类类似，但它使用的关键字是 trait。
  */
class TraitDemo {

}
object TraitDemo{

  /**
    * 2.举例：trait中带属性带方法实现
        注意：
        继承的多个trait中如果有同名的方法和属性，必须要在类中使用“override”重新定义。
        trait中不可以传参数
    */
  trait Read {
    val readType = "Read"
    val gender = "m"
    def read(name:String){
      println(name+" is reading")
    }
  }

  trait Listen {
    val listenType = "Listen"
    val gender = "m"
    def listen(name:String){
      println(name + " is listenning")
    }
  }

  class Person() extends Read with Listen{
    override val gender = "f"
  }

  /**
    *3.举例：trait中带方法不实现
    */

  trait Equle{
    def isEqule(x:Any) :Boolean
    def isNotEqule(x : Any)  = {
      !isEqule(x)
    }
  }

  class Point(x:Int, y:Int) extends Equle {
    val xx = x
    val yy = y

    def isEqule(p:Any) = {
      p.isInstanceOf[Point] && p.asInstanceOf[Point].xx==xx
    }

  }

  def main(args: Array[String]): Unit = {

    /**
      * 2.举例：trait中带属性带方法实现
      */
    val person = new Person()
    person.read("zhangsan")
    person.listen("lisi")
    println(person.listenType)
    println(person.readType)
    println(person.gender)

    /**
      * 3.举例：trait中带方法不实现
      */
    val p1 = new Point(1,2)
    val p2 = new Point(1,3)
    println(p1.isEqule(p2))
    println(p1.isNotEqule(p2))

  }

}
