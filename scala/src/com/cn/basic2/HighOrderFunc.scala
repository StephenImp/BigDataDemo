package com.cn.basic2

/**
  * Created by MOZi on 2019/4/19.
  *
  * 高阶函数
  *
  * 这里就类比于 lambda 咯
  */
class HighOrderFunc {

}

/**
  * object HighOrderFunc  是  class HighOrderFunc 的伴生对象
  */
object HighOrderFunc extends App{

  val l = List(1,2,3,4,5,6,7,8,9)

  //①
  //val newList = l.map((x:Int) => 2*x)
  //newList.foreach(x => println(x))

  //②
  //l.map(x=>2*x)

  //③   foreach 没有值。。。
  //println(l.foreach(3* _))

  //④ 对比 ③  map
  //println(l.map( 3* _))

  //⑤
  //val l1 = List(1,2,3)
  val s1 = Set(1,2,1)
  //s1.foreach(x => println(x))

  //⑥ 元组
  var hostPort = ("localhost","8080",666)
  //println(hostPort)
  //println(hostPort._1)


  //⑦ 元组 做 模式匹配？
  val t = (1, 3.14, "Fred")
  println(t._2)


  //创建元组
  //println("a" -> "b")


  //map
  //println(Map("a" -> "b","c"->"d"))


  //⑧ Option(Some  None)
  val m = Map(1 ->2 )
  //println(m.get(1))
  //println(m.get(1).get)


  //⑨ filter
  //println(l.filter(x => x%2 ==0))
  //println(l.filter(_ %2 ==0))


  //⑩zip    List((1,4), (2,5), (3,6))
  val a  = List(1,2,3)
  val b  = List(4,5,6,7)
  //println(a.zip(b))
  //println(b.zip(a))


  //12  partition
  //println(l.partition(_%2 ==0))

  //13.flatten    List(a, b, c, d)
  val l1 = List(List("a","b"),List("c","d"))
  println(l1.flatten)


  //14.flatMap(map+flatten)
  val l2 = List(List(1,2),List(3,4))
  //println(l2.flatMap(x => x.map( _ * 2)))


  //15 : _*    ，告诉编译器你希望将某个参数当作参数序列处理！
  val l3 =List(3,2,5,9,8,6,1)
  val s = List(l: _*)
  //println(s)

  //16.指代一个集合中的每个元素。例如我们要在一个Array a中筛出偶数，并乘以2
  val c = a.filter(_%2==0).map(2*_)
  //println(c)

}
