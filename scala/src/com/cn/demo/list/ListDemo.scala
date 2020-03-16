package com.cn.demo.list

/**
  * Created by MOZi on 2019/5/16.
  *
  * 1.创建list
    val list = List(1,2,3,4)
    Nil长度为0的list

    2.list遍历
    foreach ，for

    3.list方法举例
    filter:过滤元素
    count:计算符合条件的元素个数
    map：对元素操作
    flatmap ：压扁扁平,先map再flat
  */
class ListDemo {

//    //创建
//    val list = List(1,2,3,4,5)
//
//    //遍历
//    list.foreach { x => println(x)}
//    //    list.foreach { println}
//    //filter
//    val list1  = list.filter { x => x>3 }
//    list1.foreach { println}
//
//    //count
//    val value = list1.count { x => x>3 }
//    println(value)

    //map
    val nameList = List(
      "hello bjsxt",
      "hello xasxt",
      "hello shsxt"
    )
    val mapResult:List[Array[String]] = nameList.map{ x => x.split(" ") }
    mapResult.foreach{println}

    //flatmap
//    val flatMapResult : List[String] = nameList.flatMap{ x => x.split(" ") }
//    flatMapResult.foreach { println }

}
object ListDemo{

  def main(args: Array[String]): Unit = {

    val list = new ListDemo
  }
}
