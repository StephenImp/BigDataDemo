package com.cn.demo.set

/**
  * Created by MOZi on 2019/5/16.
  *
  * 1.创建set
    注意：set集合会自动去重

    2.set遍历
    foreach，for

    3.set方法举例
    交集：intersect ,&
    差集: diff ,&~
    子集:subsetOf
    最大:max
    最小:min
    转成数组，toList
    转成字符串：mkString(“~”)
  */
class SetDemo {

      //创建
      val set1 = Set(1,2,3,4,4)
      val set2 = Set(1,2,5)
      //遍历
      //注意：set会自动去重
      set1.foreach { println}
      for(s <- set1){
        println(s)
      }
      println("*******")
      /**
        * 方法举例
        */

      //交集
      val set3 = set1.intersect(set2)
      set3.foreach{println}
      val set4 = set1.&(set2)
      set4.foreach{println}
      println("*******")
      //差集
      set1.diff(set2).foreach { println }
      set1.&~(set2).foreach { println }
      //子集
      set1.subsetOf(set2)

      //最大值
      println(set1.max)
      //最小值
      println(set1.min)
      println("****")

      //转成数组，list
      set1.toArray.foreach{println}
      println("****")
      set1.toList.foreach{println}

      //mkString
      println(set1.mkString)
      println(set1.mkString("\t"))

}

object SetDemo{

  def main(args: Array[String]): Unit = {

    val setDemo = new SetDemo

  }

}
